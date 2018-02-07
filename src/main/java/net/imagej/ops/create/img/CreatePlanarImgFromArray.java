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
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

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
import net.imglib2.img.NativeImgFactory;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ArrayDataAccess;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.img.basictypeaccess.array.LongArray;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.planar.PlanarImg;
import net.imglib2.img.planar.PlanarImgFactory;
import net.imglib2.img.planar.PlanarImgs;
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

public class CreatePlanarImgFromArray {
	
	// hide constructor
	private CreatePlanarImgFromArray() {
		
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Bit extends FromArray<long[], BitType> {

		@Override
		protected void updateEPE() { entitiesPerElement = 1 / 64f; }
		
		@Override
		public Img<BitType> asPlanarImg(long[][] in) {
			return null;
		}
	}

	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint2 extends FromArray<long[], Unsigned2BitType> {

		@Override
		protected void updateEPE() { entitiesPerElement = 2 / 64f; }
		
		@Override
		public Img<Unsigned2BitType> asPlanarImg(long[][] in) {
			return null;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint4 extends FromArray<long[], Unsigned4BitType> {
		
		@Override
		protected void updateEPE() { entitiesPerElement = 4 / 64f; }
		
		@Override
		public Img<Unsigned4BitType> asPlanarImg(long[][] in) {
			return null;
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
			return null;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint12 extends FromArray<long[], Unsigned12BitType> {
		
		@Override
		protected void updateEPE() { entitiesPerElement = 12 / 64f; }
		
		@Override
		public Img<Unsigned12BitType> asPlanarImg(long[][] in) {
			return null;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int16 extends FromArray<short[], ShortType> {
		
		@Override
		public Img<ShortType> asPlanarImg(short[][] in) {
			return null;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint16 extends FromArray<short[], UnsignedShortType> {
		
		@Override
		public Img<UnsignedShortType> asPlanarImg(short[][] in) {
			return null;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int32 extends FromArray<int[], IntType> {
		
		@Override
		public Img<IntType> asPlanarImg(int[][] in) {
			return null;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint32 extends FromArray<int[], UnsignedIntType> {
		
		@Override
		public Img<UnsignedIntType> asPlanarImg(int[][] in) {
			return null;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class ARGB32 extends FromArray<int[], ARGBType> {
		
		@Override
		public Img<ARGBType> asPlanarImg(int[][] in) {
			return null;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Int64 extends FromArray<long[], LongType> {
		
		@Override
		public Img<LongType> asPlanarImg(long[][] in) {
			return null;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint64 extends FromArray<long[], UnsignedLongType> {
		
		@Override
		public Img<UnsignedLongType> asPlanarImg(long[][] in) {
			return null;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Uint128 extends FromArray<long[], Unsigned128BitType> {
		
		@Override
		protected void updateEPE() { entitiesPerElement = 128 / 64f; }
		
		public Img<Unsigned128BitType> asPlanarImg(long[][] in) {
			return null;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class UintVarLen extends FromArray<long[], UnsignedVariableBitLengthType> {
		
		// number of bits of the number
		@Parameter(required = true)
		private int nBits;
		
		@Override
		protected void updateEPE() { entitiesPerElement = nBits / 64f; }
		
		@Override
		public Img<UnsignedVariableBitLengthType> asPlanarImg(long[][] in) {
			return null;
		}
	}
	
	@Plugin(type = Ops.Create.Img.class, priority = Priority.HIGH_PRIORITY)
	public static class Float extends FromArray<float[], FloatType> {
		
		@Override
		public Img<FloatType> asPlanarImg(float[][] in) {
			return null;
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
		protected float entitiesPerElement = 1.0f;
		
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
			int expInLen = 1;
			for (int i = 0; i < imgDims.length; i++)
				expInLen *= imgDims[i] = dims.dimension(i);
			
			@SuppressWarnings("unchecked")
			I[] in = this.in();
			
			int sliceSize = (int) imgDims[0];
			if (imgDims.length >= 2)
				sliceSize *= imgDims[1];
			// number of elements per plane
			sliceSize /=  entitiesPerElement;
			int totalLen = 0;
			for (Object i : in) {
				if (!i.getClass().isArray() || Array.getLength(i) != sliceSize)
					return false;
				totalLen += sliceSize;
			}
			return totalLen == expInLen / entitiesPerElement;
		}
		
		@Override
		public Img<O> run(Img<O> output) {
			return null;
		}

		@SuppressWarnings("unchecked")
		protected abstract Img<O> asPlanarImg(I[] in);
		
		// update entitiesPerElement before checking size
		protected void updateEPE() {};
	}
}
