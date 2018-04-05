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

package net.imagej.ops.segment.detectJunctions;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.segment.detectRidges.DefaultDetectRidges;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.Interval;
import net.imglib2.RealInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.RealPositionable;
import net.imglib2.roi.geom.real.WritablePolyline;
import net.imglib2.roi.util.RealLocalizableRealPositionable;
import net.imglib2.util.Intervals;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Finds the junctions between a {@link ArrayList} of {@link WritablePolyline},
 * intended to be used optionally after running {@link DefaultDetectRidges} but
 * applicable to all groups of polylines.
 * <p>
 * TODO refactor the op to determine junction points between n-d
 * {@link WritablePolyline}
 * </p>
 * 
 * @author Gabe Selzer
 */
@Plugin(type = Ops.Segment.DetectJunctions.class)
public class DefaultDetectJunctions extends
	AbstractUnaryFunctionOp<List<? extends WritablePolyline>, List<RealPoint>>
	implements Ops.Segment.DetectJunctions, Contingent
{

	@Parameter(required = false)
	private double threshold = 2;

	private boolean areClose(RealPoint p1, RealPoint p2) {
		return getDistance(p1, p2) <= threshold;
	}

	private boolean areClose(RealPoint p1, List<RealPoint> points) {
		for (RealPoint p : points) {
			if (areClose(p1, p) == true) return true;
		}
		return false;
	}

	private static Interval slightlyEnlarge(RealInterval realInterval,
		long border)
	{
		return Intervals.expand(Intervals.smallestContainingInterval(realInterval),
			border);
	}

	private double getDistance(double[] point1, RealLocalizable point2) {
		return Math.sqrt(Math.pow(point2.getDoublePosition(0) - point1[0], 2) + Math
			.pow(point2.getDoublePosition(1) - point1[1], 2));
	}

	private double getDistance(RealLocalizable point1, RealLocalizable point2) {
		return Math.sqrt(Math.pow(point2.getDoublePosition(0) - point1
			.getDoublePosition(0), 2) + Math.pow(point2.getDoublePosition(1) - point1
				.getDoublePosition(1), 2));
	}

	private RealPoint makeRealPoint(RealLocalizableRealPositionable input) {
		return new RealPoint(input);
	}

	@Override
	public List<RealPoint> calculate(List<? extends WritablePolyline> input) {

		// output that allows for both split polyline inputs and a
		// realPointCollection for our junctions.
		List<RealPoint> output = new ArrayList<>();

		for (int first = 0; first < input.size() - 1; first++) {
			WritablePolyline firstLine = input.get(first);
			for (int second = first + 1; second < input.size(); second++) {
				WritablePolyline secondLine = input.get(second);
				// interval containing both plines
				Interval intersect = Intervals.intersect(slightlyEnlarge(firstLine, 2),
					slightlyEnlarge(secondLine, 2));
				// if the two do not intersect, then don't bother checking them against
				// each other.
				if (Intervals.isEmpty(intersect)) continue;

				// create an arraylist to contain all of the junctions for these two
				// lines (so that we can filter the junctions before putting them in
				// output).
				ArrayList<RealPoint> currentPairJunctions = new ArrayList<>();

				for (int p = 0; p < firstLine.numVertices() - 1; p++) {
					for (int q = 0; q < secondLine.numVertices() - 1; q++) {
						RealLocalizableRealPositionable p1 = firstLine.vertex(p);
						RealLocalizableRealPositionable p2 = firstLine.vertex(p + 1);
						RealLocalizableRealPositionable q1 = secondLine.vertex(q);
						RealLocalizableRealPositionable q2 = secondLine.vertex(q + 1);

						// special cases if both lines are vertical
						boolean pVertical = Math.round(p1.getDoublePosition(0)) == Math
							.round(p2.getDoublePosition(0));
						boolean qVertical = Math.round(q1.getDoublePosition(0)) == Math
							.round(q2.getDoublePosition(0));

						// intersection point between the lines created by line segments p
						// and q.
						double[] intersectionPoint = new double[2];

						// if both p and q are vertical, then p and q cannot intersect,
						// since they are parallel and cannot be the same.
						if (pVertical && qVertical) {
							parallelRoutine(p1, p2, q1, q2, currentPairJunctions, true);
							continue;
						}
						else if (pVertical) {
							double mq = (q2.getDoublePosition(1) - q1.getDoublePosition(1)) /
								(q2.getDoublePosition(0) - q1.getDoublePosition(0));
							double bq = (q1.getDoublePosition(1) - mq * q1.getDoublePosition(
								0));
							double x = p1.getDoublePosition(0);
							double y = mq * x + bq;
							intersectionPoint[0] = x;
							intersectionPoint[1] = y;
						}
						else if (qVertical) {
							double mp = (p2.getDoublePosition(1) - p1.getDoublePosition(1)) /
								(p2.getDoublePosition(0) - p1.getDoublePosition(0));
							double bp = (p1.getDoublePosition(1) - mp * p1.getDoublePosition(
								0));
							double x = q1.getDoublePosition(0);
							double y = mp * x + bp;
							intersectionPoint[0] = x;
							intersectionPoint[1] = y;
						}
						else {

							double mp = (p2.getDoublePosition(1) - p1.getDoublePosition(1)) /
								(p2.getDoublePosition(0) - p1.getDoublePosition(0));
							double mq = (q2.getDoublePosition(1) - q1.getDoublePosition(1)) /
								(q2.getDoublePosition(0) - q1.getDoublePosition(0));

							if (mp == mq) {
								parallelRoutine(p1, p2, q1, q2, currentPairJunctions, false);
								continue;
							}

							double bp = (p2.getDoublePosition(1) - mp * p2.getDoublePosition(
								0));
							double bq = (q2.getDoublePosition(1) - mq * q2.getDoublePosition(
								0));

							// point of intersection of lines created by line segments p and
							// q.
							double x = (bq - bp) / (mp - mq);
							double y = mp * x + bp;
							intersectionPoint[0] = x;
							intersectionPoint[1] = y;
						}

						// find the distance from the intersection point to both line
						// segments, and the length of the line segments.
						double distp1 = getDistance(intersectionPoint, p1);
						double distp2 = getDistance(intersectionPoint, p2);
						double distq1 = getDistance(intersectionPoint, q1);
						double distq2 = getDistance(intersectionPoint, q2);

						// max distance from line segment to intersection point
						double maxDist = Math.max(Math.min(distp1, distp2), Math.min(distq1,
							distq2));

						// if the maximum distance is close enough to the two lines, then
						// these lines are close enough to form a junction
						if (maxDist <= threshold) currentPairJunctions.add(new RealPoint(
							intersectionPoint));
					}
				}
				// filter out the current pair's junctions by removing duplicates and
				// then averaging all remaining nearby junctions
				filterJunctions(currentPairJunctions);

				// add the filtered junctions to the output list.
				for (RealPoint point : currentPairJunctions)
					output.add(point);
			}
		}

		// filter the junctions -- for each set of junctions that seem vaguely
		// similar, pick out the best one-
		filterJunctions(output);

		return output;
	}

	private void filterJunctions(List<RealPoint> list) {
		// filter out all vaguely similar junction points.
		for (int i = 0; i < list.size() - 1; i++) {
			ArrayList<RealPoint> similars = new ArrayList<>();
			similars.add(list.get(i));
			list.remove(i);
			for (int j = 0; j < list.size(); j++) {
				if (areClose(list.get(j), similars)) {
					similars.add(list.get(j));
					list.remove(j);
					j--;
				}
			}
			if (list.size() > 0) list.add(i, averagePoints(similars));
			else list.add(averagePoints(similars));
		}
	}

	private RealPoint averagePoints(ArrayList<RealPoint> list) {
		double[] pos = { 0, 0 };
		for (RealPoint p : list) {
			pos[0] += p.getDoublePosition(0);
			pos[1] += p.getDoublePosition(1);
		}
		pos[0] /= list.size();
		pos[1] /= list.size();
		return new RealPoint(pos);
	}

	private <L extends RealLocalizable & RealPositionable> void parallelRoutine(
		RealLocalizableRealPositionable p1, RealLocalizableRealPositionable p2,
		RealLocalizableRealPositionable q1, RealLocalizableRealPositionable q2,
		List<RealPoint> junctions, boolean areVertical)
	{

		// find out whether or not they are on the same line
		boolean sameLine = false;
		if (areVertical && Math.round(p1.getDoublePosition(0)) == Math.round(q1
			.getDoublePosition(0))) sameLine = true;
		else {
			double m = (q2.getDoublePosition(1) - q1.getDoublePosition(1)) / (q2
				.getDoublePosition(0) - q1.getDoublePosition(0));
			double bp = (p2.getDoublePosition(1) - m * p2.getDoublePosition(0));
			double bq = (q2.getDoublePosition(1) - m * q2.getDoublePosition(0));

			if (bp == bq) sameLine = true;
		}

		// if the two line segments do not belong to the same line, then if the
		// minimum distance between the two points is greater than the threshold,
		// there is no junction
		if (!sameLine && Math.min(Math.min(getDistance(p1, q1), getDistance(p2,
			q1)), Math.min(getDistance(p1, q2), getDistance(p2, q2))) > threshold)
			return;

		int foundJunctions = 0;
		double lengthp = getDistance(p1, p2);
		double lengthq = getDistance(q1, q2);
		// if p and q are segments on the same line, then p1, p2, q1, and q2 can all
		// be junctions. There can be at most 2 junctions between these two line
		// segments.
		// check p1 to be a junction
		if ((getDistance(p1, q1) < lengthq && getDistance(p1, q2) < lengthq &&
			sameLine) || Math.min(getDistance(p1, q1), getDistance(p1,
				q2)) < threshold)
		{
			junctions.add(makeRealPoint(p1));
			foundJunctions++;
		}
		// check p2 to be a junction
		if ((getDistance(p2, q1) < lengthq && getDistance(p2, q2) < lengthq &&
			sameLine) || Math.min(getDistance(p2, q1), getDistance(p2,
				q2)) < threshold)
		{
			junctions.add(makeRealPoint(p2));
			foundJunctions++;
		}

		// check q1 to be a junction
		if (((getDistance(q1, p1) < lengthp && getDistance(q1, p2) < lengthp &&
			sameLine) || (Math.min(getDistance(q1, p1), getDistance(q1,
				p2)) < threshold)) && foundJunctions < 2)
		{
			junctions.add(makeRealPoint(q1));
			foundJunctions++;
		}

		// check q2 to be a junction
		if (((getDistance(q2, p1) < lengthp && getDistance(q2, p2) < lengthp &&
			sameLine) || (Math.min(getDistance(q2, p1), getDistance(q2,
				p2)) < threshold)) && foundJunctions < 2)
		{
			junctions.add(makeRealPoint(q2));
			foundJunctions++;
		}
	}

	@Override
	public boolean conforms() {
		if (in().size() < 1) return false;
		return in().get(0).vertex(0).numDimensions() == 2;
	}

}
