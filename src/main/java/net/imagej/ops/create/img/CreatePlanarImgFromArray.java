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
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.img.basictypeaccess.array.FloatArray;
import net.imglib2.img.basictypeaccess.array.IntArray;
import net.imglib2.img.basictypeaccess.array.LongArray;
import net.imglib2.img.basictypeaccess.array.ShortArray;
import net.imglib2.img.planar.PlanarImg;
import net.imglib2.img.planar.PlanarImgFactory;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.*;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

/**
 * Create an {@link PlanarImg} from an array using its type
 * {@code T}. The first two dimensions (first one if it is
 * the only dimension) in parameter dims are used as sizes
 * of planes. Any other dimensions are flattened to produce
 * a list of planar slices. 
 *
 * @author Dasong Gao
 */
@SuppressWarnings("unchecked")
public class CreatePlanarImgFromArray {
	
	// hide constructor
	private CreatePlanarImgFromArray() {
		
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Bit extends FromArray<long[], BitType> {

		@Override
		protected void updateEPE() { elementsPerPixel = 1 / 64f; }
		
		@Override
		public Img<BitType> asPlanarImg(long[][] in) {
			PlanarImg<BitType, LongArray> output = 
					(PlanarImg<BitType, LongArray>) factory.create(imgDims, new BitType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new LongArray(in[i]));
			return output;
		}
	}

	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint2 extends FromArray<long[], Unsigned2BitType> {

		@Override
		protected void updateEPE() { elementsPerPixel = 2 / 64f; }
		
		@Override
		public Img<Unsigned2BitType> asPlanarImg(long[][] in) {
			PlanarImg<Unsigned2BitType, LongArray> output = 
					(PlanarImg<Unsigned2BitType, LongArray>) factory.create(imgDims, new Unsigned2BitType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new LongArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint4 extends FromArray<long[], Unsigned4BitType> {
		
		@Override
		protected void updateEPE() { elementsPerPixel = 4 / 64f; }
		
		@Override
		public Img<Unsigned4BitType> asPlanarImg(long[][] in) {
			PlanarImg<Unsigned4BitType, LongArray> output = 
					(PlanarImg<Unsigned4BitType, LongArray>) factory.create(imgDims, new Unsigned4BitType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new LongArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int8 extends FromArray<byte[], ByteType> {
		
		@Override
		public Img<ByteType> asPlanarImg(byte[][] in) {
			PlanarImg<ByteType, ByteArray> output = 
					(PlanarImg<ByteType, ByteArray>) factory.create(imgDims, new ByteType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new ByteArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint8 extends FromArray<byte[], UnsignedByteType> {
		
		@Override
		public Img<UnsignedByteType> asPlanarImg(byte[][] in) {
			PlanarImg<UnsignedByteType, ByteArray> output = 
					(PlanarImg<UnsignedByteType, ByteArray>) factory.create(imgDims, new UnsignedByteType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new ByteArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint12 extends FromArray<long[], Unsigned12BitType> {
		
		@Override
		protected void updateEPE() { elementsPerPixel = 12 / 64f; }
		
		@Override
		public Img<Unsigned12BitType> asPlanarImg(long[][] in) {
			PlanarImg<Unsigned12BitType, LongArray> output = 
					(PlanarImg<Unsigned12BitType, LongArray>) factory.create(imgDims, new Unsigned12BitType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new LongArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int16 extends FromArray<short[], ShortType> {
		
		@Override
		public Img<ShortType> asPlanarImg(short[][] in) {
			PlanarImg<ShortType, ShortArray> output = 
					(PlanarImg<ShortType, ShortArray>) factory.create(imgDims, new ShortType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new ShortArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint16 extends FromArray<short[], UnsignedShortType> {
		
		@Override
		public Img<UnsignedShortType> asPlanarImg(short[][] in) {
			PlanarImg<UnsignedShortType, ShortArray> output = 
					(PlanarImg<UnsignedShortType, ShortArray>) factory.create(imgDims, new UnsignedShortType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new ShortArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int32 extends FromArray<int[], IntType> {
		
		@Override
		public Img<IntType> asPlanarImg(int[][] in) {
			PlanarImg<IntType, IntArray> output = 
					(PlanarImg<IntType, IntArray>) factory.create(imgDims, new IntType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new IntArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint32 extends FromArray<int[], UnsignedIntType> {
		
		@Override
		public Img<UnsignedIntType> asPlanarImg(int[][] in) {
			PlanarImg<UnsignedIntType, IntArray> output = 
					(PlanarImg<UnsignedIntType, IntArray>) factory.create(imgDims, new UnsignedIntType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new IntArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class ARGB32 extends FromArray<int[], ARGBType> {
		
		@Override
		public Img<ARGBType> asPlanarImg(int[][] in) {
			PlanarImg<ARGBType, IntArray> output = 
					(PlanarImg<ARGBType, IntArray>) factory.create(imgDims, new ARGBType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new IntArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int64 extends FromArray<long[], LongType> {
		
		@Override
		public Img<LongType> asPlanarImg(long[][] in) {
			PlanarImg<LongType, LongArray> output = 
					(PlanarImg<LongType, LongArray>) factory.create(imgDims, new LongType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new LongArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint64 extends FromArray<long[], UnsignedLongType> {
		
		@Override
		public Img<UnsignedLongType> asPlanarImg(long[][] in) {
			PlanarImg<UnsignedLongType, LongArray> output = 
					(PlanarImg<UnsignedLongType, LongArray>) factory.create(imgDims, new UnsignedLongType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new LongArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint128 extends FromArray<long[], Unsigned128BitType> {
		
		@Override
		protected void updateEPE() { elementsPerPixel = 128 / 64f; }
		
		public Img<Unsigned128BitType> asPlanarImg(long[][] in) {
			PlanarImg<Unsigned128BitType, LongArray> output = 
					(PlanarImg<Unsigned128BitType, LongArray>) factory.create(imgDims, new Unsigned128BitType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new LongArray(in[i]));
			return output;
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
		public Img<UnsignedVariableBitLengthType> asPlanarImg(long[][] in) {
			PlanarImg<UnsignedVariableBitLengthType, LongArray> output = 
					(PlanarImg<UnsignedVariableBitLengthType, LongArray>) factory.create(imgDims,
							new UnsignedVariableBitLengthType(nBits));
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new LongArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Float extends FromArray<float[], FloatType> {
		
		@Override
		public Img<FloatType> asPlanarImg(float[][] in) {
			PlanarImg<FloatType,FloatArray> output = 
					(PlanarImg<FloatType, FloatArray>) factory.create(imgDims, new FloatType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new FloatArray(in[i]));
			return output;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Double extends FromArray<double[], DoubleType> {
		
		@Override
		public Img<DoubleType> asPlanarImg(double[][] in) {
			PlanarImg<DoubleType, DoubleArray> output = 
					(PlanarImg<DoubleType, DoubleArray>) factory.create(imgDims, new DoubleType());
			for (int i = 0; i < in.length; i++)
				output.setPlane(i, new DoubleArray(in[i]));
			return output;
		}
	}

	// helper class for wrapping
	private static abstract class FromArray<I, O extends NativeType<O>>
			extends AbstractUnaryFunctionOp<I[], Img<O>>
			implements Ops.Create.Img, Contingent {
		
		// image dimensions
		@Parameter(required = true)
		private Dimensions dims;
		
		// image dimensions (unpacked from dims)
		protected long[] imgDims;
		
		// used to scale for Bit, 12Bit, 128Bit, varBit, etc.
		protected float elementsPerPixel = 1.0f;
		
		protected PlanarImgFactory<O> factory = new PlanarImgFactory<O>();
		
		@Override
		public Img<O> calculate(final I[] inArray) {
			return this.asPlanarImg(inArray);
		}
		
		@Override
		public boolean conforms() {
			updateEPE();
			imgDims = new long[dims.numDimensions()];
			if (imgDims.length == 0)
				return false;
			int numPlane = 1;
			for (int i = 0; i < imgDims.length; i++) {
				imgDims[i] = dims.dimension(i);
				if (i >= 2)
					numPlane *= imgDims[i];
			}
			
			I[] in = this.in();
			
			int sliceSize = (int) imgDims[0];
			if (imgDims.length >= 2)
				sliceSize *= imgDims[1];
			// number of array elements for a single plane
			int elementsPerPlane = (int) Math.ceil(sliceSize * elementsPerPixel);
			for (Object i : in) {
				if (!i.getClass().isArray() || Array.getLength(i) != elementsPerPlane)
					return false;
			}
			return in.length == numPlane;
		}
		
		@Override
		public Img<O> run(Img<O> output) {
			return null;
		}

		protected abstract Img<O> asPlanarImg(I[] in);
		
		// update entitiesPerElement before checking size
		protected void updateEPE() {};
	}
}
