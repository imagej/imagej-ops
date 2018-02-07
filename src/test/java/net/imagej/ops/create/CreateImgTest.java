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

package net.imagej.ops.create;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.create.img.CreateArrayImgFromArray;
import net.imagej.ops.create.img.CreateImgFromDimsAndType;
import net.imagej.ops.create.img.CreateImgFromImg;
import net.imagej.ops.create.img.CreateImgFromInterval;
import net.imagej.ops.create.img.CreatePlanarImgFromArray;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.planar.PlanarImgFactory;
import net.imglib2.img.planar.PlanarImgs;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedVariableBitLengthType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Intervals;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.junit.Test;

/**
 * Tests several ways to create an image
 *
 * @author Daniel Seebacher (University of Konstanz)
 * @author Tim-Oliver Buchholz (University of Konstanz)
 */
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateImgTest extends AbstractOpTest {

	private static final int TEST_SIZE = 100;

	@Test
	public void testImageMinimum() {

		final Random randomGenerator = new Random();

		for (int i = 0; i < TEST_SIZE; i++) {

			// between 2 and 5 dimensions
			final long[] max = new long[randomGenerator.nextInt(4) + 2];
			final long[] min = new long[max.length];

			// between 2 and 10 pixels per dimensions
			for (int j = 0; j < max.length; j++) {
				max[j] = randomGenerator.nextInt(9) + 2;
				min[j] = Math.max(0, max[j] - randomGenerator.nextInt(4));
			}

			// create img
			final Img<?> img = (Img<?>) ops.run(CreateImgFromInterval.class,
				new FinalInterval(min, max));

			assertArrayEquals("Image Minimum:", min, Intervals.minAsLongArray(img));
			assertArrayEquals("Image Maximum:", max, Intervals.maxAsLongArray(img));
		}
	}

	@Test
	public void testImageDimensions() {

		final Random randomGenerator = new Random();

		for (int i = 0; i < TEST_SIZE; i++) {

			// between 2 and 5 dimensions
			final long[] dim = new long[randomGenerator.nextInt(4) + 2];

			// between 2 and 10 pixels per dimensions
			for (int j = 0; j < dim.length; j++) {
				dim[j] = randomGenerator.nextInt(9) + 2;
			}

			// create img
			@SuppressWarnings("unchecked")
			final Img<DoubleType> img = (Img<DoubleType>) ops.run(
				CreateImgFromDimsAndType.class, dim, new DoubleType());

			assertArrayEquals("Image Dimensions:", dim, Intervals
				.dimensionsAsLongArray(img));
		}
	}

	@Test
	public void testImgFromImg() {
		// create img
		final Img<ByteType> img =
			ops.create().img(new FinalDimensions(1), new ByteType());
		@SuppressWarnings("unchecked")
		final Img<ByteType> newImg = (Img<ByteType>) ops.run(CreateImgFromImg.class,
			img);

		// should both be ByteType. New Img shouldn't be DoubleType (default)
		assertEquals(img.firstElement().getClass(), newImg.firstElement()
			.getClass());
	}
	
	// -- FromArray
	
	//@Test
	public void testImgFromArrayShow() {
		final long[] dim = {250, 250, 250};
		
		int srcLen = 1;
		for (int j = 0; j < dim.length; j++) {
			srcLen *= dim[j];
		}
		
		byte[] arr = new byte[srcLen];
		
//		for (int j = 0; j < srcLen; j++)
//			arr[j] = (byte) (j%256);
		for (int i = 0; i < dim[0]; i++)
			for (int j = 0; j < dim[1]; j++)
				for (int k = 0; k < dim[2]; k++)
					if (i*i+j*j+k*k<200*200)
						arr[i+j*250+k*250*250] = (byte) 250;
		
		@SuppressWarnings("unchecked")
		final Img<UnsignedByteType> img = (Img<UnsignedByteType>) ops.run(
				CreateArrayImgFromArray.Uint8.class, arr, dim);
		net.imglib2.img.display.imagej.ImageJFunctions.show(img);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testImgFromArrayValue() {
		final Random randomGenerator = new Random();
		// test different types of imgs
		final ImgFactory<?>[] facs = {
				new ArrayImgFactory<DoubleType>(), 
				new PlanarImgFactory<DoubleType>()
			};

		for (int i = 0; i < TEST_SIZE; i++) {

			// between 1 and 5 dimensions
			final long[] dim = new long[randomGenerator.nextInt(4) + 2];
			
			// source array length
			int srcLen = 1;
			
			// between 2 and 10 pixels per dimensions
			for (int j = 0; j < dim.length; j++) {
				dim[j] = randomGenerator.nextInt(9) + 2;
				srcLen *= dim[j];
			}
			
			double[] arr = new double[srcLen];
			
			// arr = { 0, 0.1, 0.2, 0.3, ... }
			for (int j = 0; j < srcLen; j++)
				arr[j] = 0.1 * j;
			
			Img<DoubleType> img = null;
			
			if ((i & 1) == 0) {
				img = (Img<DoubleType>) ops.run(
						CreateArrayImgFromArray.Double.class, arr, dim);
			}
			else {
				int sliceLen = (int) dim[0];
				if (dim.length >= 2)
					sliceLen *= dim[1];
				double[][] arr2d = new double[srcLen / sliceLen][sliceLen];
				double n = 0;
				for (int j = 0; j < arr2d.length; j++)
					for (int k = 0; k < arr2d[0].length; k++) {
						arr2d[j][k] = n;
						n += 0.1;
					}
				img = (Img<DoubleType>) ops.run(
						CreatePlanarImgFromArray.Double.class, arr2d, dim);
			}
			
			/*(Img<DoubleType>) ops.run(
				CreateImgFromArray.Double.class, arr, dim, facs[i % 2]);*/
			RandomAccess<DoubleType> ra = img.randomAccess();
			
			// check random pixels
			int[] coord = new int[dim.length];
			for (int j = 0; j < 10; j++) {
				int inputIndex = 0;
				int sliceSize = 1;
				for (int k = 0; k < dim.length; k++) {
					coord[k] = randomGenerator.nextInt((int) dim[k]);
					inputIndex += coord[k] * sliceSize;
					sliceSize *= dim[k];
				}
				ra.setPosition(coord);
				assertEquals("Pixel value: ", ra.get().getRealDouble(), arr[inputIndex], 0.05);
			}
		}
	}
	
	@Test
	public void testImgFromArrayFactory() {
		final Dimensions dim = new FinalDimensions(1, 1, 1);
		final long[] in = {1};
		
		@SuppressWarnings("unchecked")
		final Img<UnsignedVariableBitLengthType> arrayImg = 
				(Img<UnsignedVariableBitLengthType>) ops.run(
				CreateArrayImgFromArray.UintVarLen.class, in, dim, 5);
		final Class<?> arrayFactoryClass = arrayImg.factory().getClass();
		assertEquals("Image Factory: ", ArrayImgFactory.class, arrayFactoryClass);

		@SuppressWarnings("unchecked")
		final Img<UnsignedVariableBitLengthType> planarImg = 
				(Img<UnsignedVariableBitLengthType>) ops.run(
				CreatePlanarImgFromArray.UintVarLen.class, new long[][] {in}, dim, 5);
		final Class<?> planarFactoryClass = planarImg.factory().getClass();
		assertEquals("Image Factory: ", CellImgFactory.class, planarFactoryClass);
	}
	
	@Test
	public void testImgFromArrayType() {
		final Dimensions dim = new FinalDimensions(1, 1, 1);
		final long[] in = {1};
		
		@SuppressWarnings("unchecked")
		final Img<UnsignedVariableBitLengthType> uvblImg = 
				(Img<UnsignedVariableBitLengthType>) ops.run(
				CreateArrayImgFromArray.UintVarLen.class, in, dim, 5);
		final Class<?> uvblImgClass = uvblImg.firstElement().getClass();
		assertEquals("Image Type: ", UnsignedVariableBitLengthType.class, uvblImgClass);
		
		@SuppressWarnings("unchecked")
		final Img<ARGBType> argbImg = 
				(Img<ARGBType>) ops.run(
				CreateArrayImgFromArray.ARGB32.class, in, dim);
		final Class<?> argbImgClass = argbImg.firstElement().getClass();
		assertEquals("Image Type: ", ARGBType.class, argbImgClass);
		
		@SuppressWarnings("unchecked")
		final Img<BitType> bitImg = 
				(Img<BitType>) ops.run(
				CreateArrayImgFromArray.Bit.class, new boolean[] {true}, dim);
		final Class<?> bitImgClass = bitImg.firstElement().getClass();
		assertEquals("Image Type: ", BitType.class, bitImgClass);
	}

	@Test
	public void testImageFactory() {
		final Dimensions dim = new FinalDimensions(10, 10, 10);

		@SuppressWarnings("unchecked")
		final Img<DoubleType> arrayImg = (Img<DoubleType>) ops.run(
			CreateImgFromDimsAndType.class, dim, new DoubleType(),
			new ArrayImgFactory<DoubleType>());
		final Class<?> arrayFactoryClass = arrayImg.factory().getClass();
		assertEquals("Image Factory: ", ArrayImgFactory.class, arrayFactoryClass);

		@SuppressWarnings("unchecked")
		final Img<DoubleType> cellImg = (Img<DoubleType>) ops.run(
			CreateImgFromDimsAndType.class, dim, new DoubleType(),
			new CellImgFactory<DoubleType>());
		final Class<?> cellFactoryClass = cellImg.factory().getClass();
		assertEquals("Image Factory: ", CellImgFactory.class, cellFactoryClass);
	}

	@Test
	public void testImageType() {
		final Dimensions dim = new FinalDimensions(10, 10, 10);

		assertEquals("Image Type: ", BitType.class, ((Img<?>) ops.run(
			CreateImgFromDimsAndType.class, dim, new BitType())).firstElement()
				.getClass());

		assertEquals("Image Type: ", ByteType.class, ((Img<?>) ops.run(
			CreateImgFromDimsAndType.class, dim, new ByteType())).firstElement()
				.getClass());

		assertEquals("Image Type: ", UnsignedByteType.class, ((Img<?>) ops.create()
			.img(dim, new UnsignedByteType())).firstElement().getClass());

		assertEquals("Image Type: ", IntType.class, ((Img<?>) ops.run(
			CreateImgFromDimsAndType.class, dim, new IntType())).firstElement()
				.getClass());

		assertEquals("Image Type: ", FloatType.class, ((Img<?>) ops.run(
			CreateImgFromDimsAndType.class, dim, new FloatType())).firstElement()
				.getClass());

		assertEquals("Image Type: ", DoubleType.class, ((Img<?>) ops.run(
			CreateImgFromDimsAndType.class, dim, new DoubleType())).firstElement()
				.getClass());
	}

	@Test
	public void testCreateFromImgSameType() {
		final Img<ByteType> input = PlanarImgs.bytes(10, 10, 10);
		final Img<?> res = (Img<?>) ops.run(CreateImgFromDimsAndType.class, input,
			input.firstElement().createVariable());

		assertEquals("Image Type: ", ByteType.class, res.firstElement().getClass());
		assertArrayEquals("Image Dimensions: ", Intervals
			.dimensionsAsLongArray(input), Intervals.dimensionsAsLongArray(res));
		assertEquals("Image Factory: ", input.factory().getClass(), res.factory()
			.getClass());
	}

	@Test
	public void testCreateFromImgDifferentType() {
		final Img<ByteType> input = PlanarImgs.bytes(10, 10, 10);
		final Img<?> res = (Img<?>) ops.run(CreateImgFromDimsAndType.class, input,
			new ShortType());

		assertEquals("Image Type: ", ShortType.class, res.firstElement().getClass());
		assertArrayEquals("Image Dimensions: ", Intervals
			.dimensionsAsLongArray(input), Intervals.dimensionsAsLongArray(res));
		assertEquals("Image Factory: ", input.factory().getClass(), res.factory()
			.getClass());
	}

	@Test
	public void testCreateFromRaiDifferentType() {
		final IntervalView<ByteType> input =
			Views.interval(PlanarImgs.bytes(10, 10, 10), new FinalInterval(
				new long[] { 10, 10, 1 }));

		final Img<?> res = (Img<?>) ops.run(CreateImgFromDimsAndType.class, input,
			new ShortType());

		assertEquals("Image Type: ", ShortType.class, res.firstElement().getClass());

		assertArrayEquals("Image Dimensions: ", Intervals
			.dimensionsAsLongArray(input), Intervals.dimensionsAsLongArray(res));

		assertEquals("Image Factory: ", ArrayImgFactory.class, res.factory()
			.getClass());
	}

	/**
	 * A simple test to ensure {@link Integer} arrays are not eaten by the varargs
	 * when passed as the only argument.
	 */
	@Test
	public void testCreateFromIntegerArray() {
		final Integer[] dims = new Integer[] {25, 25, 10};

		final Img<?> res = ops.create().img(dims);

		for (int i=0; i<dims.length; i++) {
			assertEquals("Image Dimension " + i + ": ", dims[i].longValue(), res
				.dimension(i));
		}
	}

	/**
	 * A simple test to ensure {@link Long} arrays are not eaten by the varargs
	 * when passed as the only argument.
	 */
	@Test
	public void testCreateFromLongArray() {
		final Long[] dims = new Long[] {25l, 25l, 10l};

		final Img<?> res = ops.create().img(dims);

		for (int i=0; i<dims.length; i++) {
			assertEquals("Image Dimension " + i + ": ", dims[i].longValue(), res
				.dimension(i));
		}
	}

}
