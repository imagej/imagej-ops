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

import java.lang.reflect.Array;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.LongArray;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.*;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Create an {@link ArrayImg} from an array using its type
 * {@code T}.
 *
 * @author Dasong Gao
 */
@SuppressWarnings("deprecation")
public class CreateArrayImgFromArray {
	
	// hide constructor
	private CreateArrayImgFromArray() {
		
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Bit extends FromArray<long[], BitType> {

		@Override
		protected void updateEPE() { elementsPerPixel = 1 / 64f; }
		
		@Override
		public Img<BitType> asArrayImg(long[] in) {
			return ArrayImgs.bits(new LongArray(in), this.imgDims);
		}
	}

	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint2 extends FromArray<long[], Unsigned2BitType> {

		@Override
		protected void updateEPE() { elementsPerPixel = 2 / 64f; }
		
		@Override
		public Img<Unsigned2BitType> asArrayImg(long[] in) {
			return ArrayImgs.unsigned2Bits(new LongArray(in), this.imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint4 extends FromArray<long[], Unsigned4BitType> {
		
		@Override
		protected void updateEPE() { elementsPerPixel = 4 / 64f; }
		
		@Override
		public Img<Unsigned4BitType> asArrayImg(long[] in) {
			return ArrayImgs.unsigned4Bits(new LongArray(in), this.imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int8 extends FromArray<byte[], ByteType> {
		
		@Override
		public Img<ByteType> asArrayImg(byte[] in) {
			return ArrayImgs.bytes(in, this.imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint8 extends FromArray<byte[], UnsignedByteType> {
		
		@Override
		public Img<UnsignedByteType> asArrayImg(byte[] in) {
			return ArrayImgs.unsignedBytes(in, this.imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint12 extends FromArray<long[], Unsigned12BitType> {
		
		@Override
		protected void updateEPE() { elementsPerPixel = 12 / 64f; }
		
		@Override
		public Img<Unsigned12BitType> asArrayImg(long[] in) {
			return ArrayImgs.unsigned12Bits(new LongArray(in), this.imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int16 extends FromArray<short[], ShortType> {
		
		@Override
		public Img<ShortType> asArrayImg(short[] in) {
			return ArrayImgs.shorts(in, imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint16 extends FromArray<short[], UnsignedShortType> {
		
		@Override
		public Img<UnsignedShortType> asArrayImg(short[] in) {
			return ArrayImgs.unsignedShorts(in, imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int32 extends FromArray<int[], IntType> {
		
		@Override
		public Img<IntType> asArrayImg(int[] in) {
			return ArrayImgs.ints(in, imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint32 extends FromArray<int[], UnsignedIntType> {
		
		@Override
		public Img<UnsignedIntType> asArrayImg(int[] in) {
			return ArrayImgs.unsignedInts(in, imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class ARGB32 extends FromArray<int[], ARGBType> {
		
		@Override
		public Img<ARGBType> asArrayImg(int[] in) {
			return ArrayImgs.argbs(in, imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int64 extends FromArray<long[], LongType> {
		
		@Override
		public Img<LongType> asArrayImg(long[] in) {
			return ArrayImgs.longs(in, imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint64 extends FromArray<long[], UnsignedLongType> {
		
		@Override
		public Img<UnsignedLongType> asArrayImg(long[] in) {
			return ArrayImgs.unsignedLongs(in, imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint128 extends FromArray<long[], Unsigned128BitType> {
		
		@Override
		protected void updateEPE() { elementsPerPixel = 128 / 64f; }
		
		public Img<Unsigned128BitType> asArrayImg(long[] in) {
			return ArrayImgs.unsigned128Bits(new LongArray(in), imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class UintVarLen extends FromArray<long[], UnsignedVariableBitLengthType> {
		
		// number of bits of the number
		@Parameter(required = true)
		private int nBits;
		
		@Override
		protected void updateEPE() { elementsPerPixel = nBits / 64f; }
		
		@Override
		public Img<UnsignedVariableBitLengthType> asArrayImg(long[] in) {
			return ArrayImgs.unsignedVariableBitLengths(new LongArray(in), nBits, imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Float extends FromArray<float[], FloatType> {
		
		@Override
		public Img<FloatType> asArrayImg(float[] in) {
			return ArrayImgs.floats(in, this.imgDims);
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Double extends FromArray<double[], DoubleType> {
		
		@Override
		public Img<DoubleType> asArrayImg(double[] in) {
			return ArrayImgs.doubles(in, this.imgDims);
		}
	}

	// helper class for wrapping
	private static abstract class FromArray<I, O extends NativeType<O>>
			extends AbstractUnaryFunctionOp<I, Img<O>>
			implements Ops.Create.Img, Contingent {
		
		// image dimensions
		@Parameter(required = true)
		private Dimensions dims;
		
		// image dimensions (unpacked from dims)
		protected long[] imgDims;
		
		// used to scale for Bit, 12Bit, 128Bit, varBit, etc.
		protected float elementsPerPixel = 1.0f;
		
		@Override
		public Img<O> calculate(final I inArray) {
			return this.asArrayImg((I)inArray);
		}
		
		@Override
		public boolean conforms() {
			updateEPE();
			imgDims = new long[dims.numDimensions()];
			if (imgDims.length == 0)
				return false;
			int numPixel = 1;
			for (int i = 0; i < imgDims.length; i++)
				numPixel *= imgDims[i] = dims.dimension(i);
			
			I in = this.in();
			return in.getClass().isArray() && numPixel != 0 
					&& (int) Math.ceil((numPixel * elementsPerPixel)) == Array.getLength(in);
		}
		
		@Override
		public Img<O> run(Img<O> output) {
			return null;
		}

		protected abstract Img<O> asArrayImg(I in);
		
		// update entitiesPerElement before checking size
		protected void updateEPE() {};
	}
}
