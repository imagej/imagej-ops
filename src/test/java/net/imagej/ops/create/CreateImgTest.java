/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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
import net.imglib2.FinalDimensions;
import net.imglib2.FinalInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.planar.PlanarImgs;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
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
			final Img<?> img = ops.create().img(new FinalInterval(min, max));

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
			final Img<?> img = (Img<?>) ops.create().img(dim);

			assertArrayEquals("Image Dimensions:", dim, Intervals
				.dimensionsAsLongArray(img));
		}
	}

	@Test
	public void testImgFromImg() {
		// create img
		final Img<ByteType> img =
			ops.create().img(new FinalDimensions(1), new ByteType());
		final Img<ByteType> newImg = ops.create().img(img);

		// should both be ByteType. New Img shouldn't be DoubleType (default)
		assertEquals(img.firstElement().getClass(), newImg.firstElement()
			.getClass());
	}

	@Test
	public void testImageFactory() {

		final long[] dim = new long[] { 10, 10, 10 };

		assertEquals("Image Factory: ", ArrayImgFactory.class, ((Img<?>) ops
			.create().img(dim, null, new ArrayImgFactory<DoubleType>())).factory()
			.getClass());

		assertEquals("Image Factory: ", CellImgFactory.class, ((Img<?>) ops
			.create().img(dim, null, new CellImgFactory<DoubleType>())).factory()
			.getClass());

	}

	@Test
	public void testImageType() {

		final long[] dim = new long[] { 10, 10, 10 };

		assertEquals("Image Type: ", BitType.class, ((Img<?>) ops.create().img(dim,
			new BitType())).firstElement().getClass());

		assertEquals("Image Type: ", ByteType.class, ((Img<?>) ops.create().img(
			dim, new ByteType())).firstElement().getClass());

		assertEquals("Image Type: ", UnsignedByteType.class, ((Img<?>) ops.create()
			.img(dim, new UnsignedByteType())).firstElement().getClass());

		assertEquals("Image Type: ", IntType.class, ((Img<?>) ops.create().img(dim,
			new IntType())).firstElement().getClass());

		assertEquals("Image Type: ", FloatType.class, ((Img<?>) ops.create().img(
			dim, new FloatType())).firstElement().getClass());

		assertEquals("Image Type: ", DoubleType.class, ((Img<?>) ops.create().img(
			dim, new DoubleType())).firstElement().getClass());
	}

	@Test
	public void testCreateFromImgSameType() {

		final Img<ByteType> input = PlanarImgs.bytes(10, 10, 10);
		final Img<?> res =
			ops.create().img(input, input.firstElement().createVariable());

		assertEquals("Image Type: ", ByteType.class, res.firstElement().getClass());
		assertArrayEquals("Image Dimensions: ", Intervals
			.dimensionsAsLongArray(input), Intervals.dimensionsAsLongArray(res));
		assertEquals("Image Factory: ", input.factory().getClass(), res.factory()
			.getClass());
	}

	@Test
	public void testCreateFromImgDifferentType() {

		final Img<ByteType> input = PlanarImgs.bytes(10, 10, 10);
		final Img<?> res = ops.create().img(input, new ShortType());

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

		final Img<?> res = ops.create().img(input, new ShortType());

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

		final Img<?> res = (Img<?>) ops.create().img(dims);

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

		final Img<?> res = (Img<?>) ops.create().img(dims);

		for (int i=0; i<dims.length; i++) {
			assertEquals("Image Dimension " + i + ": ", dims[i].longValue(), res
				.dimension(i));
		}
	}

}
