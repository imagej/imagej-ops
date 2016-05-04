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
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.Composite;

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

	private UnaryFunctionOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<? extends Composite<I>>> hessian;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		hessian = (UnaryFunctionOp) Functions.unary(ops(), net.imagej.ops.features.shape.DefaultHessian.class,
				RandomAccessibleInterval.class, RandomAccessibleInterval.class);
	}

	/**
	 * TODO Improve documentation
	 * 
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

		RandomAccessibleInterval<? extends Composite<I>> hessianRAI = hessian.compute1(input);

		float factor = 2 / (float) Math.PI;

		Cursor<FloatType> shapeIndexCursor = Views.iterable(output).cursor();
		Cursor<? extends Composite<I>> hessianCursor = Views.iterable(hessianRAI).cursor();

		// Iterate
		while (shapeIndexCursor.hasNext()) {
			// Move all cursors forward by one pixel
			shapeIndexCursor.fwd();
			hessianCursor.fwd();

			float dnx_x = -hessianCursor.get().get(0).getRealFloat();
			float dnx_y = -hessianCursor.get().get(1).getRealFloat();
			float dny_x = -hessianCursor.get().get(2).getRealFloat();
			float dny_y = -hessianCursor.get().get(3).getRealFloat();

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
