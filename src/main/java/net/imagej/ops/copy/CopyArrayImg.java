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

package net.imagej.ops.copy;

import java.lang.reflect.Array;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.array.ArrayDataAccess;
import net.imglib2.type.NativeType;
import net.imglib2.util.Intervals;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Copying {@link ArrayImg} into another {@link ArrayImg}
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = Ops.Copy.Img.class, priority = Priority.VERY_HIGH)
public class CopyArrayImg<T extends NativeType<T>, A extends ArrayDataAccess<A>>
		extends
		AbstractUnaryHybridCF<ArrayImg<T, A>, ArrayImg<T, A>>
		implements Ops.Copy.Img, Contingent {

	@Override
	public ArrayImg<T, A> createOutput(final ArrayImg<T, A> input) {
		// NB: Workaround for ArrayImgFactory not overriding create(Dimensions, T).
		final long[] dims = new long[ input.numDimensions() ];
		input.dimensions( dims );
		final ArrayImg<T, ?> copy =
			input.factory().create(dims, input.firstElement().createVariable());

		// TODO: Find a way to guarantee the type.
		@SuppressWarnings("unchecked")
		final ArrayImg<T, A> typedCopy = (ArrayImg<T, A>) copy;
		return typedCopy;
	}

	@Override
	public void compute(final ArrayImg<T, A> input,
			final ArrayImg<T, A> output) {

		final Object inArray = input.update(null).getCurrentStorageArray();
		System.arraycopy(inArray, 0, output.update(null)
				.getCurrentStorageArray(), 0, Array.getLength(inArray));

	}

	@Override
	public boolean conforms() {
		if (out() == null) return true;
		return Intervals.equalDimensions(in(), out());
	}
}
