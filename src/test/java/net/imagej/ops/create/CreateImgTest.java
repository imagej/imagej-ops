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
import net.imagej.ops.create.CreateOps.CreateImg;
import net.imglib2.FinalInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.cell.CellImgFactory;
import net.imglib2.img.planar.PlanarImgs;
import net.imglib2.type.NativeType;
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
 * @author Daniel Seebacher, University of Konstanz.
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */

public class CreateImgTest<T extends NativeType<T>> extends AbstractOpTest {

	private static final int TEST_SIZE = 100;

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
			final Img<?> img = (Img<?>) ops.run(CreateImg.class, dim);

			assertArrayEquals("Image Dimensions:", dim, Intervals
				.dimensionsAsLongArray(img));
		}
	}

	@Test
	public void testImgFromImg() {
		long[] dim = new long[] { 1 };
		// create img
		final Img<?> img = (Img<?>) ops.run(CreateImg.class, dim, new ByteType());
		final Img<?> newImg = (Img<?>) ops.run(CreateImg.class, img);

		// should both be ByteType. New Img shouldn't be DoubleType (default)
		assertEquals(img.firstElement().getClass(), newImg.firstElement()
			.getClass());
	}

	@Test
	public void testImageFactory() {

		final long[] dim = new long[] { 10, 10, 10 };

		assertEquals("Image Factory: ", ArrayImgFactory.class, ((Img<?>) ops.run(
			CreateImg.class, dim, null, new ArrayImgFactory<DoubleType>())).factory()
			.getClass());

		assertEquals("Image Factory: ", CellImgFactory.class, ((Img<?>) ops.run(
			CreateImg.class, dim, null, new CellImgFactory<DoubleType>())).factory()
			.getClass());

	}

	@Test
	public void testImageType() {

		final long[] dim = new long[] { 10, 10, 10 };

		assertEquals("Image Type: ", BitType.class, ((Img<?>) ops.run(
			CreateImg.class, dim, new BitType(), null)).firstElement().getClass());

		assertEquals("Image Type: ", ByteType.class, ((Img<?>) ops.run(
			CreateImg.class, dim, new ByteType(), null)).firstElement().getClass());

		assertEquals("Image Type: ", UnsignedByteType.class, ((Img<?>) ops.run(
			CreateImg.class, dim, new UnsignedByteType(), null)).firstElement()
			.getClass());

		assertEquals("Image Type: ", IntType.class, ((Img<?>) ops.run(
			CreateImg.class, dim, new IntType(), null)).firstElement().getClass());

		assertEquals("Image Type: ", FloatType.class, ((Img<?>) ops.run(
			CreateImg.class, dim, new FloatType(), null)).firstElement().getClass());

		assertEquals("Image Type: ", DoubleType.class, ((Img<?>) ops.run(
			CreateImg.class, dim, new DoubleType(), null)).firstElement().getClass());
	}

	@Test
	public void testCreateFromImgSameType() {

		final Img<ByteType> input = PlanarImgs.bytes(10, 10, 10);
		final Img<?> res =
			((Img<?>) ops.run(CreateImg.class, input, input.firstElement()
				.createVariable()));

		assertEquals("Image Type: ", ByteType.class, res.firstElement().getClass());
		assertArrayEquals("Image Dimensions: ", Intervals
			.dimensionsAsLongArray(input), Intervals.dimensionsAsLongArray(res));
		assertEquals("Image Factory: ", input.factory().getClass(), res.factory()
			.getClass());
	}

	@Test
	public void testCreateFromImgDifferentType() {

		final Img<ByteType> input = PlanarImgs.bytes(10, 10, 10);
		final Img<?> res =
			((Img<?>) ops.run(CreateImg.class, input, new ShortType()));

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

		final Img<?> res =
			((Img<?>) ops.run(CreateImg.class, input, new ShortType()));

		assertEquals("Image Type: ", ShortType.class, res.firstElement().getClass());

		assertArrayEquals("Image Dimensions: ", Intervals
			.dimensionsAsLongArray(input), Intervals.dimensionsAsLongArray(res));

		assertEquals("Image Factory: ", ArrayImgFactory.class, res.factory()
			.getClass());
	}

}
