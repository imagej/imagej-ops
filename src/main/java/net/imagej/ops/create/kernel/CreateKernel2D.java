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

package net.imagej.ops.create.kernel;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This Op takes a 2-D array of explicit values and creates a simple image, e.g.
 * for use as a kernel in convolution.
 *
 * @author Mark Hiner hinerm at gmail.com
 */
@Plugin(type = Ops.Create.Kernel.class)
public class CreateKernel2D<T extends ComplexType<T>> extends
	AbstractUnaryFunctionOp<double[][], RandomAccessibleInterval<T>> implements
	Ops.Create.Kernel, Contingent
{

	@Parameter(required = false)
	private T type;

	private UnaryFunctionOp<Interval, RandomAccessibleInterval<T>> createOp;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		createOp = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			RandomAccessibleInterval.class, Interval.class, type);
	}

	@Override
	public boolean conforms() {
		// check the nested arrays for nulls
		for (int i = 0; i < in().length; i++)
			if (in()[i] == null) return false;
		return true;
	}

	@Override
	public RandomAccessibleInterval<T> calculate(double[][] input) {
		final long[] dims = { input.length, input[0].length };
		final RandomAccessibleInterval<T> rai = createOp.calculate(new FinalInterval(
			dims));

		final Cursor<T> cursor = Views.iterable(rai).cursor();
		for (int j = 0; j < input.length; j++) {
			for (int k = 0; k < input[j].length; k++) {
				cursor.fwd();

				cursor.get().setReal(input[j][k]);
			}
		}

		return rai;
	}
}
