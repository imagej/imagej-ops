/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.scalepixel;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Op;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.type.numeric.integer.ByteType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = ScalePixel.NAME, priority = Priority.HIGH_PRIORITY)
public class ScalePixelsArrayImgByteType
		extends
		AbstractFunction<ArrayImg<ByteType, ByteArray>, ArrayImg<ByteType, ByteArray>>
		implements
		ScalePixel<ArrayImg<ByteType, ByteArray>, ArrayImg<ByteType, ByteArray>> {

	@Parameter
	private double oldMin;

	@Parameter
	private double newMin;

	@Parameter
	private double factor;

	@Override
	public ArrayImg<ByteType, ByteArray> compute(
			final ArrayImg<ByteType, ByteArray> input,
			final ArrayImg<ByteType, ByteArray> output) {

		byte[] inputContainer = input.update(null).getCurrentStorageArray();
		byte[] outputContainer = output.update(null).getCurrentStorageArray();

		for (int i = 0; i < inputContainer.length; i++) {
			outputContainer[i] = (byte) (((inputContainer[i] - oldMin) * factor) + newMin);
		}

		return output;
	}

}
