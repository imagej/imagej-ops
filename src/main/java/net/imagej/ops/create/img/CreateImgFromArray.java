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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.*;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Create an {@link Img} from an array using its type
 * {@code T}.
 *
 * @author Dasong Gao
 */

public class CreateImgFromArray {
	
	// hide constructor
	private CreateImgFromArray() {
		
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Bit extends FromArray<boolean[], BitType> {
		@Override
		public void convert(boolean[] in, List<BitType> out) {
			for (boolean b : in)
				out.add(new BitType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint2 extends FromArray<long[], Unsigned2BitType> {
		@Override
		public void convert(long[] in, List<Unsigned2BitType> out) {
			for (long b : in)
				out.add(new Unsigned2BitType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint4 extends FromArray<long[], Unsigned4BitType> {
		@Override
		public void convert(long[] in, List<Unsigned4BitType> out) {
			for (long b : in)
				out.add(new Unsigned4BitType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int8 extends FromArray<byte[], ByteType> {
		@Override
		public void convert(byte[] in, List<ByteType> out) {
			for (byte b : in)
				out.add(new ByteType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint8 extends FromArray<byte[], UnsignedByteType> {
		@Override
		public void convert(byte[] in, List<UnsignedByteType> out) {
			for (byte b : in)
				out.add(new UnsignedByteType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int16 extends FromArray<short[], ShortType> {
		@Override
		public void convert(short[] in, List<ShortType> out) {
			for (short b : in)
				out.add(new ShortType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint16 extends FromArray<short[], UnsignedShortType> {
		@Override
		public void convert(short[] in, List<UnsignedShortType> out) {
			for (short b : in)
				out.add(new UnsignedShortType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int32 extends FromArray<int[], IntType> {
		@Override
		public void convert(int[] in, List<IntType> out) {
			for (int b : in)
				out.add(new IntType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint32 extends FromArray<int[], UnsignedIntType> {
		@Override
		public void convert(int[] in, List<UnsignedIntType> out) {
			for (int b : in)
				out.add(new UnsignedIntType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class ARGB32 extends FromArray<int[], ARGBType> {
		@Override
		public void convert(int[] in, List<ARGBType> out) {
			for (int b : in)
				out.add(new ARGBType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int64 extends FromArray<long[], LongType> {
		@Override
		public void convert(long[] in, List<LongType> out) {
			for (long b : in)
				out.add(new LongType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint64 extends FromArray<long[], UnsignedLongType> {
		@Override
		public void convert(long[] in, List<UnsignedLongType> out) {
			for (long b : in)
				out.add(new UnsignedLongType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint128 extends FromArray<BigInteger[], Unsigned128BitType> {
		@Override
		public void convert(BigInteger[] in, List<Unsigned128BitType> out) {
			for (BigInteger b : in)
				out.add(new Unsigned128BitType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class UintVarLen extends FromArray<long[], UnsignedVariableBitLengthType> {
		
		// number of bits of the number
		@Parameter(required = true)
		private int nBits;
		
		@Override
		public void convert(long[] in, List<UnsignedVariableBitLengthType> out) {
			for (long b : in)
				out.add(new UnsignedVariableBitLengthType(b, nBits));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Float extends FromArray<float[], FloatType> {
		@Override
		public void convert(float[] in, List<FloatType> out) {
			for (float b : in)
				out.add(new FloatType(b));
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Double extends FromArray<double[], DoubleType> {
		@Override
		public void convert(double[] in, List<DoubleType> out) {
			for (double b : in)
				out.add(new DoubleType(b));
		}
	}
	
	// helper class
	private static abstract class FromArray<I, O extends NativeType<O>>
			extends AbstractUnaryFunctionOp<I, Img<O>>
			implements Ops.Create.Img, Contingent {
		
		// image dimensions
		@Parameter(required = true)
		private Dimensions dims;
		
		// types of img to create (array, cell, planar)
		@Parameter(required = false)
		private ImgFactory<O> factory;
		
		// image dimensions (unpacked from dims)
		private long[] imgDims;
		
		private Img<O> output;

		@Override
		public Img<O> calculate(final I inArray) {
			// creates default factory if not provided
			if (factory == null) {
				factory = dims == null ? ops().create().imgFactory() :
					ops().create().imgFactory(dims);
			}
			
			List<O> nativeTypeArr = new ArrayList<O>();
			convert(inArray, nativeTypeArr);
			O outputType = nativeTypeArr.get(0);
			
			// create output Img
			output = Imgs.create((ImgFactory<O>) factory, dims, outputType);
			
			// fill
			Cursor<O> cursor = output.cursor();
			while (cursor.hasNext()) {
				O value = cursor.next();
				int inputIndex = 0;
				// indexing based on coordinate in each dimension
				for (int i = 0; i < imgDims.length; i++)
					inputIndex += cursor.getLongPosition(i) * (i - 1 < 0 ? 1 : imgDims[i - 1]);
				value.set(nativeTypeArr.get(inputIndex));
			}
			return output;
		}
		
		@Override
		public boolean conforms() {
			// expected length for input array
			int expInLen = 1;
			imgDims = new long[dims.numDimensions()];
			// 0D image not allowed
			if (imgDims.length == 0)
				return false;
			for (int i = 0; i < imgDims.length; i++)
				expInLen *= imgDims[i] = dims.dimension(i);
			
			I in = this.in();
			
			// checks if input length matches dimensions
			return in.getClass().isArray() && expInLen != 0 && Array.getLength(in) == expInLen;
		}
		
		// abstract method for converting input array (primitive or BigInteger)
		// into a list of outputTypes
		public abstract void convert(I in, List<O> out);/*{
			if (in.length <= 0)
				return null;
			final O[] outArr = (O[]) Array.newInstance(outputType.getClass(), in.length);
			
			Class<?> inClass = in[0].getClass();
			Class<?> outClass = outputType.getClass();
			
			for (int i = 0; i < in.length; i++)
				outArr[i] = (O) (
						outClass == ARGBType.class ? new ARGBType((int)in[i]) :
						outClass == BitType.class ?  new BitType((Boolean)in[i]) :
						outClass == ByteType.class ? new ByteType((byte)in[i]) :
						outClass == ShortType.class ? new ShortType((short)in[i]) :
						outClass == IntType.class ?  new IntType((int)in[i]) :
						outClass == LongType.class ? new LongType((long)in[i]) :
							
						outClass == Unsigned2BitType.class ? new Unsigned2BitType((long)in[i]) :
						outClass == Unsigned4BitType.class ? new Unsigned4BitType((long)in[i]) :
						outClass == UnsignedByteType.class ? new UnsignedByteType((byte)in[i]) :
						outClass == Unsigned12BitType.class ? new Unsigned12BitType((long)in[i]) :
						outClass == UnsignedShortType.class ? new UnsignedShortType((short)in[i]) :
						outClass == UnsignedIntType.class ?  new UnsignedIntType((int)in[i]) :
						outClass == UnsignedLongType.class ? new UnsignedLongType((long)in[i]) :
						//outClass == UnsignedVariableBitLengthType.class ?  new UnsignedVariableBitLengthType((int)in[i]) :
						outClass == Unsigned128BitType.class ? new Unsigned128BitType((BigInteger)in[i]) :
						//outClass == ComplexDoubleType.class ?  new ComplexDoubleType
						//outClass == ComplexFloatType.class ?  new ComplexFloatType
						outClass == FloatType.class ? new FloatType((float)in[i]) :
						outClass == DoubleType.class ? new DoubleType((double)in[i]) : null
						);
					;
			return outArr;
		}*/
	}
}
