/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.threshold.apply;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imagej.ops.threshold.ThresholdNamespace;
import net.imagej.ops.threshold.localBernsen.LocalBernsenThreshold;
import net.imagej.ops.threshold.localContrast.LocalContrastThreshold;
import net.imagej.ops.threshold.localMean.LocalMeanThreshold;
import net.imagej.ops.threshold.localMean.LocalMeanThresholdIntegral;
import net.imagej.ops.threshold.localMedian.LocalMedianThreshold;
import net.imagej.ops.threshold.localMidGrey.LocalMidGreyThreshold;
import net.imagej.ops.threshold.localNiblack.LocalNiblackThreshold;
import net.imagej.ops.threshold.localNiblack.LocalNiblackThresholdIntegral;
import net.imagej.ops.threshold.localPhansalkar.LocalPhansalkarThreshold;
import net.imagej.ops.threshold.localPhansalkar.LocalPhansalkarThresholdIntegral;
import net.imagej.ops.threshold.localSauvola.LocalSauvolaThreshold;
import net.imagej.ops.threshold.localSauvola.LocalSauvolaThresholdIntegral;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.ByteArray;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link LocalThreshold} and various {@link LocalThresholdMethod}s.
 *
 * @author Jonathan Hale
 * @author Martin Horn
 * @see LocalThreshold
 * @see LocalThresholdMethod
 */
public class LocalThresholdTest extends AbstractOpTest {

	Img<ByteType> in;
	Img<BitType> out;

	/**
	 * Initialize images.
	 *
	 * @throws Exception
	 */
	@Before
	public void before() throws Exception {
		in = generateByteArrayTestImg(true, new long[] { 10, 10 });

		out = in.factory().imgFactory(new BitType()).create(in, new BitType());
	}

	/**
	 * Test whether parameters for ops in {@link ThresholdNamespace} opmethods are
	 * correctly set.
	 */
	@Test
	public void testOpMethods() {
		ops.threshold().localMeanThreshold(out, in, new RectangleShape(3, false),
			0.0);
		ops.threshold().localMeanThreshold(out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, RandomAccessibleInterval<ByteType>>(
				Boundary.SINGLE), 0.0);
		ops.threshold().localMeanThreshold(out, in, new DiamondShape(3),
			0.0);
		ops.threshold().localMeanThreshold(out, in, new DiamondShape(3),
			new OutOfBoundsMirrorFactory<ByteType, RandomAccessibleInterval<ByteType>>(
				Boundary.SINGLE), 0.0);
		
		ops.threshold().localBernsenThreshold(out, in, new RectangleShape(3, false),
			1.0, Double.MAX_VALUE * 0.5);
		ops.threshold().localBernsenThreshold(out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, RandomAccessibleInterval<ByteType>>(
				Boundary.SINGLE), 1.0, Double.MAX_VALUE * 0.5);

		ops.threshold().localContrastThreshold(out, in, new RectangleShape(3,
			false));
		ops.threshold().localContrastThreshold(out, in, new RectangleShape(3,
			false),
			new OutOfBoundsMirrorFactory<ByteType, RandomAccessibleInterval<ByteType>>(
				Boundary.SINGLE));

		ops.threshold().localMedianThreshold(out, in, new RectangleShape(3, false),
			1.0);
		ops.threshold().localMedianThreshold(out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, RandomAccessibleInterval<ByteType>>(
				Boundary.SINGLE), 1.0);

		ops.threshold().localMidGreyThreshold(out, in, new RectangleShape(3, false),
			1.0);
		ops.threshold().localMidGreyThreshold(out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, RandomAccessibleInterval<ByteType>>(
				Boundary.SINGLE), 1.0);

		ops.threshold().localNiblackThreshold(out, in, new RectangleShape(3, false),
			1.0, 2.0);
		ops.threshold().localNiblackThreshold(out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, RandomAccessibleInterval<ByteType>>(
				Boundary.SINGLE), 1.0, 2.0);

		ops.threshold().localPhansalkarThreshold(out, in, new RectangleShape(3,
			false),
			new OutOfBoundsMirrorFactory<ByteType, RandomAccessibleInterval<ByteType>>(
				Boundary.SINGLE), 0.25, 0.5);
		ops.threshold().localPhansalkarThreshold(out, in, new RectangleShape(3,
			false));

		ops.threshold().localSauvolaThreshold(out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, RandomAccessibleInterval<ByteType>>(
				Boundary.SINGLE), 0.5, 0.5);
		ops.threshold().localSauvolaThreshold(out, in, new RectangleShape(3,
			false));
	}

	/**
	 * @see LocalBernsenThreshold
	 */
	@Test
	public void testLocalBernsenThreshold() {
		ops.run(LocalBernsenThreshold.class, out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE), 1.0,
			Double.MAX_VALUE * 0.5);

		assertEquals(out.firstElement().get(), true);
	}

	/**
	 * @see LocalContrastThreshold
	 */
	@Test
	public void testLocalContrastThreshold() {
		ops.run(LocalContrastThreshold.class, out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(out.firstElement().get(), false);
	}

	/**
	 * @see LocalMeanThreshold
	 */
	@Test
	public void testLocalThresholdMean() {
		ops.run(LocalMeanThreshold.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0);

		assertEquals(out.firstElement().get(), true);
	}
	
	/**
	 * @see LocalMeanThresholdIntegral
	 */
	@Test
	public void testLocalMeanThresholdIntegral() {
		ops.run(LocalMeanThresholdIntegral.class,
			out,
			in,
			new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0);
		
		assertEquals(out.firstElement().get(), true);
	}

	/**
	 * @see LocalMeanThresholdIntegral
	 * @see LocalMeanThreshold
	 */
	@SuppressWarnings("null")
	@Test
	public void testLocalMeanResultsConsistency() {
		Img<BitType> out2 = null;
		Img<BitType> out3 = null;
		try {
			out2 = in.factory().imgFactory(new BitType()).create(in, new BitType());
			out3 = in.factory().imgFactory(new BitType()).create(in, new BitType());
		}
		catch (IncompatibleTypeException exc) {
			exc.printStackTrace();
		}
		
		// Default implementation
		ops.run(LocalMeanThreshold.class, out2, in, new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0);
		
		// Integral image-based implementation
		ops.run(LocalMeanThresholdIntegral.class,
			out3,
			in,
			new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0);
		
		testIterableIntervalSimilarity(out2, out3);
	}

	/**
	 * @see LocalMedianThreshold
	 */
	@Test
	public void testLocalMedianThreshold() {
		ops.run(LocalMedianThreshold.class, out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0);

		assertEquals(out.firstElement().get(), true);
	}

	/**
	 * @see LocalMidGreyThreshold
	 */
	@Test
	public void testLocalMidGreyThreshold() {
		ops.run(LocalMidGreyThreshold.class, out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE), 0.0);
		
		assertEquals(out.firstElement().get(), true);
	}

	/**
	 * @see LocalNiblackThreshold
	 */
	@Test
	public void testLocalNiblackThreshold() {
		ops.run(LocalNiblackThreshold.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);

		assertEquals(out.firstElement().get(), true);
	}

	/**
	 * @see LocalNiblackThresholdIntegral
	 */
	@Test
	public void testLocalNiblackThresholdIntegral() {
		ops.run(LocalNiblackThresholdIntegral.class, out, in, new RectangleShape(3,
			false), new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(
				Boundary.SINGLE), 0.0, 0.0);

		assertEquals(out.firstElement().get(), true);
	}

	/**
	 * @see LocalNiblackThresholdIntegral
	 * @see LocalNiblackThreshold
	 */
	@Test
	public void testLocalNiblackResultsConsistency() {
		Img<BitType> out2 = null;
		Img<BitType> out3 = null;
		try {
			out2 = in.factory().imgFactory(new BitType()).create(in, new BitType());
			out3 = in.factory().imgFactory(new BitType()).create(in, new BitType());
		}
		catch (IncompatibleTypeException exc) {
			exc.printStackTrace();
		}
		
		// Default implementation
		ops.run(LocalNiblackThreshold.class, out2, in, new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);
		
		// Integral image-based implementation
		ops.run(LocalNiblackThresholdIntegral.class,
			out3,
			in,
			new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);
		
		testIterableIntervalSimilarity(out2, out3);
	}

	/**
	 * @see LocalPhansalkarThreshold
	 */
	@Test
	public void testLocalPhansalkar() {
		ops.run(LocalPhansalkarThreshold.class, out, in, new RectangleShape(1,
			false), new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(
				Boundary.SINGLE), 0.0, 0.0);

		assertEquals(out.firstElement().get(), false);
	}

	/**
	 * @see LocalPhansalkarThresholdIntegral
	 */
	@Test
	public void testLocalPhansalkarIntegral() {
		ops.run(LocalPhansalkarThresholdIntegral.class, out, in, new RectangleShape(3,
			false), null, 0.0, 0.0);

		assertEquals(out.firstElement().get(), false);
	}

	/**
	 * @see LocalPhansalkarThresholdIntegral
	 * @see LocalPhansalkarThreshold
	 */
	@Test
	public void testLocalPhansalkarResultsConsistency() {
		Img<BitType> out2 = null;
		Img<BitType> out3 = null;
		try {
			out2 = in.factory().imgFactory(new BitType()).create(in, new BitType());
			out3 = in.factory().imgFactory(new BitType()).create(in, new BitType());
		}
		catch (IncompatibleTypeException exc) {
			exc.printStackTrace();
		}
		
		// Default implementation
		ops.run(LocalPhansalkarThreshold.class, out2, in, new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);
		
		// Integral image-based implementation
		ops.run(LocalPhansalkarThresholdIntegral.class,
			out3,
			in,
			new RectangleShape(2, false), null,
			0.0, 0.0);
		
		testIterableIntervalSimilarity(out2, out3);
	}
	
	/**
	 * @see LocalSauvolaThreshold
	 */
	@Test
	public void testLocalSauvola() {
		ops.run(LocalSauvolaThreshold.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);

		assertEquals(out.firstElement().get(), false);
	}

	/**
	 * @see LocalSauvolaThresholdIntegral
	 */
	@Test
	public void testLocalSauvolaIntegral() {
		ops.run(LocalSauvolaThresholdIntegral.class, out, in, new RectangleShape(3, false), null,
			0.0, 0.0);

		assertEquals(out.firstElement().get(), false);
	}

	/**
	 * @see LocalSauvolaThresholdIntegral
	 * @see LocalSauvolaThreshold
	 */
	@Test
	public void testLocalSauvolaResultsConsistency() {
		Img<BitType> out2 = null;
		Img<BitType> out3 = null;
		try {
			out2 = in.factory().imgFactory(new BitType()).create(in, new BitType());
			out3 = in.factory().imgFactory(new BitType()).create(in, new BitType());
		}
		catch (IncompatibleTypeException exc) {
			exc.printStackTrace();
		}
		
		// Default implementation
		ops.run(LocalSauvolaThreshold.class, out2, in, new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);
		
		// Integral image-based implementation
		ops.run(LocalSauvolaThresholdIntegral.class,
			out3,
			in,
			new RectangleShape(2, false), null,
			0.0, 0.0);
		
		testIterableIntervalSimilarity(out2, out3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testContingencyOfNormalImplementation() {
		ops.run(LocalSauvolaThreshold.class, out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);
	}

	public ArrayImg<ByteType, ByteArray> generateKnownByteArrayTestImgSmall() {
		final long[] dims = new long[] { 2, 2 };
		final byte[] array = new byte[4];

		array[0] = (byte) 10;
		array[1] = (byte) 20;
		array[2] = (byte) 30;
		array[3] = (byte) 40;

		return ArrayImgs.bytes(array, dims);
	}

	public ArrayImg<ByteType, ByteArray> generateKnownByteArrayTestImgLarge() {
		final long[] dims = new long[] { 3, 3 };
		final byte[] array = new byte[9];

		array[0] = (byte) 40;
		array[1] = (byte) 40;
		array[2] = (byte) 20;

		array[3] = (byte) 40;
		array[4] = (byte) 40;
		array[5] = (byte) 20;

		array[6] = (byte) 20;
		array[7] = (byte) 20;
		array[8] = (byte) 100;

		return ArrayImgs.bytes(array, dims);
	}

	/**
	 * Checks if two {@link IterableInterval} have the same content.
	 *
	 * @param ii1
	 * @param ii2
	 */
	public static <T extends RealType<T>, S extends RealType<S>> void testIterableIntervalSimilarity(IterableInterval<T> ii1,
			IterableInterval<S> ii2) {
		// Test for pixel-wise equality of the results
		Cursor<T> cursor1 = ii1.localizingCursor();
		Cursor<S> cursor2 = ii2.cursor();
		while (cursor1.hasNext() && cursor2.hasNext()) {
			T value1 = cursor1.next();
			S value2 = cursor2.next();

			assertEquals(value1.getRealDouble(), value2.getRealDouble(), 0.00001d);
		}
	}

}
