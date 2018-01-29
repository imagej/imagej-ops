/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 ImageJ developers.
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

package net.imagej.ops.create.img;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.IterableInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Create an {@link Img} from an array using its type
 * {@code T}.
 *
 * @author Dasong Gao
 * @param <T>
 */
@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
public class CreateImgFromArray<T extends NativeType<T>> extends AbstractUnaryFunctionOp<byte[], Img<T>> implements
Ops.Create.Img {
	
	@Parameter(required = true)
	private Dimensions dims;
	
	@Parameter(required = false)
	private ImgFactory<T> factory;
	
	private Img<T> output;
	
	private T type;
	
	public static void main(String[] args) {
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public Img<T> calculate(final byte[] input) {
		//output = (Img<T>) ops().run(CreateImgFromDimsAndType.class, dims, new UnsignedByteType());
		//output = (Img<T>) ops().run(Ops.Create.Img.class, dims, new UnsignedByteType());
		
		
		
		type = (T) new UnsignedByteType();
		if (factory == null) {
			factory = dims == null ? ops().create().imgFactory() :
				ops().create().imgFactory(dims);
		}
		output = Imgs.create((ImgFactory<T>) factory, dims, type);
		long[] imgDims = new long[dims.numDimensions()];
		for (int i = 0; i < imgDims.length; i++)
			imgDims[i] = dims.dimension(i);
		Cursor<T> cursor = output.cursor();
		while (cursor.hasNext()) {
			T value = cursor.next();
			int inputIndex = 0;
			for (int i = 0; i < imgDims.length; i++)
				inputIndex += cursor.getLongPosition(i) * (i - 1 < 0 ? 1 : imgDims[i - 1]);
			value.set((T) new UnsignedByteType(input[inputIndex]));
		}
		return output;
	}

}
