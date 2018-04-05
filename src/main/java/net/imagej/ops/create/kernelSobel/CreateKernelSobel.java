/* #%L
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

package net.imagej.ops.create.kernelSobel;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractNullaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Creates a separated sobel kernel.
 * 
 * @author Eike Heinz, University of Konstanz
 *
 * @param <T>
 *            type of input
 */

@Plugin(type = Ops.Create.KernelSobel.class, name = Ops.Create.KernelSobel.NAME)
public class CreateKernelSobel<T extends ComplexType<T>>
		extends AbstractNullaryFunctionOp<RandomAccessibleInterval<T>> implements Ops.Create.KernelSobel {

	@Parameter
	private T type;

	private static final float[] values = { 1.0f, 2.0f, 1.0f, -1.0f, 0.0f, 1.0f };
	
	private UnaryFunctionOp<Interval, RandomAccessibleInterval<T>> createOp;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		createOp = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class, RandomAccessibleInterval.class,
				Dimensions.class, type);
	}

	@Override
	public RandomAccessibleInterval<T> calculate() {
		long[] dim = new long[4];

		dim[0] = 3;
		dim[1] = 1;

		for (int k = 2; k < dim.length; k++) {
			dim[k] = 1;
		}

		dim[dim.length - 1] = 2;

		RandomAccessibleInterval<T> output = createOp.calculate(new FinalInterval(dim));
		final Cursor<T> cursor = Views.iterable(output).cursor();
		int i = 0;
		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.get().setReal(values[i]);
			i++;
		}

		return output;
	}

}
