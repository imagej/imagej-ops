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

package net.imagej.ops.filter.pad;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Filter.PadFFTInput;
import net.imagej.ops.filter.fftSize.ComputeFFTMethodsSize;
import net.imagej.ops.special.function.Functions;
import net.imglib2.Dimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.numeric.ComplexType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Op used to pad the image to a size that is compatible with FFTMethods
 * 
 * @author bnorthan
 * @param <T>
 * @param <I>
 * @param <O>
 */
@Plugin(type = Ops.Filter.PadFFTInput.class, priority = Priority.HIGH)
public class PadInputFFTMethods<T extends ComplexType<T>, I extends RandomAccessibleInterval<T>, O extends RandomAccessibleInterval<T>>
		extends PadInputFFT<T, I, O> implements PadFFTInput {

	@Parameter(required = false)
	private boolean fast = true;

	/**
	 * The OutOfBoundsFactory used to extend the image
	 */
	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> obf = null;

	@Override
	public void initialize() {
		super.initialize();

		setFFTSizeOp(Functions.unary(ops(), ComputeFFTMethodsSize.class, long[][].class, Dimensions.class, true, fast));

		if (obf != null) {
			setObf(obf);
		}
	}

}
