/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.segment.detectRidges;

import net.imglib2.Cursor;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

public class RidgeDetectionUtils {

	/**
	 * Returns the angle between the x-axis and a given vector (in components).
	 * Always positive.
	 * 
	 * @param y - double denoting the y component of the vector.
	 * @param x - double denoting the x component of the vector.
	 * @return double denoting the angle between the vector and the x-axis.
	 */
	protected static double getAngle(double x, double y) {

		double angle = 0;

		if (y > 0 && x == 0) {
			angle = 90;
		}
		else if (y < 0 && x == 0) {
			angle = 270;
		}
		else {

			// calculate the angle and convert it to degrees.
			angle = Math.atan(y / x) * 180 / Math.PI;
			// if x is negative then arctan will return the angle - 180 degrees, but
			// we want the actual angle.
			if (x < 0) angle += 180;
			// we always want a positive angle
			if (angle < 0) angle += 360;
		}

		return angle;
	}

	/**
	 * This method determines the octant (neighboring pixel) a vector points
	 * towards in pixel space. Explanation: Given a pixel denoted as '0', the
	 * neighboring pixels are numbered counterclockwise around pixel 0, beginning
	 * with pixel 1 to the right of pixel 0, and ending with pixel 8 one down and
	 * one right from pixel 0. The method matches this number to a range of
	 * degrees, e.g. a vector in octant 2 would have an angle between 22.5 degrees
	 * and 67.5 degrees. This is helpful to determine which neighboring pixels to
	 * look at in line detection.
	 *
	 * @param y - double denoting the y component of the vector.
	 * @param x - double denoting the x component of the vector.
	 * @return int denoting the octant in which a vector is in.
	 */
	protected static int getOctant(double x, double y) {
		int octant = 1;
		double angle = getAngle(x, y);

		while (angle > 22.5) {
			octant++;
			angle -= 45;
		}
		while(octant > 8) octant -= 8;
		while(octant < 1) octant += 8;

		return octant;
	}

	/**
	 * Creates the modifier array for the octants.
	 * 
	 * @param octant - describes the octant modifier needed.
	 * @return double[] of length 2, with the first number denoting the modifier
	 *         to the x-coordinate and the second denoting the modifier to the
	 *         y-coordinate.
	 */
	protected static int[] getOctantCoords(int octant) {
		int[] coords = new int[2];
		switch (octant) {
			case 9:
			case 1:
				coords[0] = 1;
				break;
			case 2:
			case 10:
				coords[0] = 1;
				coords[1] = 1;
				break;
			case 3:
			case 11:
				coords[1] = 1;
				break;
			case 4:
			case 12:
				coords[0] = -1;
				coords[1] = 1;
				break;
			case 5:
				coords[0] = -1;
				break;
			case 6:
				coords[0] = -1;
				coords[1] = -1;
				break;
			case 7:
			case -1:
				coords[1] = -1;
				break;
			case 0:
			case 8:
				coords[0] = 1;
				coords[1] = -1;
				break;
		}
		return coords;
	}

	/**
	 * Helper method to take a point in a n-d image and reduce it down to a 2
	 * dimensional point (e.g. in 3D cartesian space removing the z-coordinate
	 * from the point).
	 * 
	 * @param RA - the random access of the {@code n>2} dimensional image.
	 * @return Point in 2D space.
	 */
	protected static Point get2DPoint(RandomAccess<DoubleType> RA) {
		long[] coords = new long[2];
		coords[0] = RA.getLongPosition(0);
		coords[1] = RA.getLongPosition(1);

		return new Point(coords);
	}

	/**
	 * Helper method to take a point in a n-d image and reduce it down to a 2
	 * dimensional Realpoint (e.g. in 3D cartesian space removing the z-coordinate
	 * from the point).
	 * 
	 * @param RA - the random access of the {@code n>2} dimensional image.
	 * @return Point in 2D space.
	 */
	protected static RealPoint get2DRealPoint(RandomAccess<DoubleType> RA) {
		double[] coords = new double[2];
		coords[0] = RA.getDoublePosition(0);
		coords[1] = RA.getDoublePosition(1);

		return new RealPoint(coords);
	}

	/**
	 * Returns a {@link RealPoint} with the coordinates (x, y).
	 * 
	 * @param x - the x-coordinate
	 * @param y - the y-coordinate
	 * @return a {@link RealPoint} with coordinates (x,y)
	 */
	protected static RealPoint get2DRealPoint(double x, double y) {
		return new RealPoint(new double[] { x, y });
	}

	protected static long[] getMaxCoords(
		RandomAccessibleInterval<DoubleType> input, boolean useAbsoluteValue)
	{
		long[] dims = new long[input.numDimensions()];
		double max = Double.MIN_VALUE;
		Cursor<DoubleType> cursor = Views.iterable(input).localizingCursor();
		while (cursor.hasNext()) {
			cursor.fwd();
			double current = useAbsoluteValue ? Math.abs(cursor.get().get()) : cursor
				.get().get();
			if (current > max) {
				max = current;
				for (int d = 0; d < input.numDimensions(); d++) {
					dims[d] = cursor.getLongPosition(d);
				}
			}
		}
		return dims;
	}

}
