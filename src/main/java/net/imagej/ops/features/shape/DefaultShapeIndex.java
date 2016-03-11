/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

package net.imagej.ops.features.shape;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

/**
 * Calculate the shape index as defined in J Koenderink and A van Doorn,
 * “Surface shape and curvature scales,” Image Vision Comput, vol. 10, no. 8,
 * pp. 557–565, 1992
 * 
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Shape.ShapeIndex.class)
public class DefaultShapeIndex<I extends RealType<I>> extends
	AbstractUnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<FloatType>>
	implements Ops.Shape.ShapeIndex
{

	private UnaryFunctionOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<I>> createRAIFromRAI;
	private UnaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<I>>[] derivativeComputers;
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		createRAIFromRAI = RAIs.function(ops(), Ops.Create.Img.class, in());
		
		derivativeComputers = new UnaryComputerOp[in().numDimensions()];
		for (int i = 0; i < in().numDimensions(); i++) {
			derivativeComputers[i] = RAIs.computer(ops(),
				Ops.Filter.DirectionalDerivative.class, in(), i);
		}

	}

	/**
	 * The formula is: dnx_x + dny_y s = 2 / PI * arctan
	 * --------------------------------------- sqrt((dnx_x - dny_y)^2 + 4 dny_x
	 * dnx_y) where _x and _y are the x and y components of the partial
	 * derivatives of the normal vector of the surface defined by the intensities
	 * of the image. n_x and n_y are the negative partial derivatives of the
	 * intensity, approximated by simple differences.
	 */
	@Override
	public void compute1(RandomAccessibleInterval<I> input,
		RandomAccessibleInterval<FloatType> output)
	{

		RandomAccessibleInterval<I> dx = createRAIFromRAI.compute1(input);
		derivativeComputers[0].compute1(input, dx);
		
		RandomAccessibleInterval<I> dy = createRAIFromRAI.compute1(input);
		derivativeComputers[1].compute1(input, dy);
		
		RandomAccessibleInterval<I> dxx = createRAIFromRAI.compute1(dx);
		derivativeComputers[0].compute1(dx, dxx);

		RandomAccessibleInterval<I> dxy = createRAIFromRAI.compute1(dx);
		derivativeComputers[1].compute1(dx, dxy);

		RandomAccessibleInterval<I> dyx = createRAIFromRAI.compute1(dy);
		derivativeComputers[0].compute1(dy, dyx);

		RandomAccessibleInterval<I> dyy = createRAIFromRAI.compute1(dy);
		derivativeComputers[1].compute1(dy, dyy);

		float factor = 2 / (float) Math.PI;

		Cursor<FloatType> shapeIndexCursor = Views.iterable(output).cursor();

		// Create cursors for all images
		Cursor<I> cursorDxx = Views.iterable(dxx).cursor();
		Cursor<I> cursorDxy = Views.iterable(dxy).cursor();
		Cursor<I> cursorDyx = Views.iterable(dyx).cursor();
		Cursor<I> cursorDyy = Views.iterable(dyy).cursor();

		// Iterate
		while (shapeIndexCursor.hasNext()) {
			// Move all cursors forward by one pixel
			shapeIndexCursor.fwd();
			cursorDxx.fwd();
			cursorDxy.fwd();
			cursorDyx.fwd();
			cursorDyy.fwd();

			float dnx_x = -cursorDxx.get().getRealFloat();
			float dnx_y = -cursorDxy.get().getRealFloat();
			float dny_x = -cursorDyx.get().getRealFloat();
			float dny_y = -cursorDyy.get().getRealFloat();

			double D = Math.sqrt((dnx_x - dny_y) * (dnx_x - dny_y) + 4 * dnx_y *
				dny_x);
			float s = factor * (float) Math.atan((dnx_x + dny_y) / D);

			FloatType sFloatType = new FloatType(s);
			FloatType zeroFloatType = new FloatType(0f);

			// set the value of this pixel of the output image to the same as the
			// input, every Type supports T.set( T type )
			shapeIndexCursor.get().set(Float.isNaN(s) ? zeroFloatType : sFloatType);
		}
	}

}
