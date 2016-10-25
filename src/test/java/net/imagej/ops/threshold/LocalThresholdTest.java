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

package net.imagej.ops.threshold;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Threshold.Huang;
import net.imagej.ops.Ops.Threshold.IJ1;
import net.imagej.ops.Ops.Threshold.Intermodes;
import net.imagej.ops.Ops.Threshold.IsoData;
import net.imagej.ops.Ops.Threshold.Li;
import net.imagej.ops.Ops.Threshold.LocalMeanThreshold;
import net.imagej.ops.Ops.Threshold.LocalMedianThreshold;
import net.imagej.ops.Ops.Threshold.LocalMidGreyThreshold;
import net.imagej.ops.Ops.Threshold.MaxEntropy;
import net.imagej.ops.Ops.Threshold.MaxLikelihood;
import net.imagej.ops.Ops.Threshold.MinError;
import net.imagej.ops.Ops.Threshold.Minimum;
import net.imagej.ops.Ops.Threshold.Moments;
import net.imagej.ops.Ops.Threshold.Otsu;
import net.imagej.ops.Ops.Threshold.Percentile;
import net.imagej.ops.Ops.Threshold.RenyiEntropy;
import net.imagej.ops.Ops.Threshold.Shanbhag;
import net.imagej.ops.Ops.Threshold.Triangle;
import net.imagej.ops.Ops.Threshold.Yen;
import net.imagej.ops.threshold.LocalThresholders.LocalHuang;
import net.imagej.ops.threshold.LocalThresholders.LocalIJ1;
import net.imagej.ops.threshold.LocalThresholders.LocalIntermodes;
import net.imagej.ops.threshold.LocalThresholders.LocalIsoData;
import net.imagej.ops.threshold.LocalThresholders.LocalLi;
import net.imagej.ops.threshold.LocalThresholders.LocalMaxEntropy;
import net.imagej.ops.threshold.LocalThresholders.LocalMaxLikelihood;
import net.imagej.ops.threshold.LocalThresholders.LocalOtsu;
import net.imagej.ops.threshold.LocalThresholders.LocalPercentile;
import net.imagej.ops.threshold.LocalThresholders.LocalRenyiEntropy;
import net.imagej.ops.threshold.LocalThresholders.LocalShanbhag;
import net.imagej.ops.threshold.LocalThresholders.LocalTriangle;
import net.imagej.ops.threshold.LocalThresholders.LocalYen;
import net.imagej.ops.threshold.localBernsen.LocalBernsen;
import net.imagej.ops.threshold.localContrast.LocalContrast;
import net.imagej.ops.threshold.localMean.IntegralLocalMean;
import net.imagej.ops.threshold.localMean.LocalMean;
import net.imagej.ops.threshold.localMedian.LocalMedian;
import net.imagej.ops.threshold.localMidGrey.LocalMidGrey;
import net.imagej.ops.threshold.localNiblack.IntegralLocalNiblack;
import net.imagej.ops.threshold.localNiblack.LocalNiblack;
import net.imagej.ops.threshold.localPhansalkar.IntegralLocalPhansalkar;
import net.imagej.ops.threshold.localPhansalkar.LocalPhansalkar;
import net.imagej.ops.threshold.localSauvola.IntegralLocalSauvola;
import net.imagej.ops.threshold.localSauvola.IntegralLocalSauvolaThresholdLearner;
import net.imagej.ops.threshold.localSauvola.LocalSauvola;
import net.imagej.ops.threshold.localSauvola.LocalSauvolaThresholdLearner;
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
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link LocalThresholder}s.
 *
 * @author Jonathan Hale
 * @author Martin Horn
 * @see LocalThresholder
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
		final OutOfBoundsMirrorFactory<ByteType, RandomAccessibleInterval<ByteType>> oobf =
			new OutOfBoundsMirrorFactory<>(Boundary.SINGLE);
		final RectangleShape rectangleShape = new RectangleShape(3, false);

		ops.threshold().localMeanThreshold(out, in, rectangleShape, 0.0);
		ops.threshold().localMeanThreshold(out, in, rectangleShape, oobf, 0.0);
		ops.threshold().localMeanThreshold(out, in, new DiamondShape(3), 0.0);
		ops.threshold().localMeanThreshold(out, in, new DiamondShape(3), oobf, 0.0);

		ops.threshold().localBernsenThreshold(out, in, rectangleShape, 1.0,
			Double.MAX_VALUE * 0.5);
		ops.threshold().localBernsenThreshold(out, in, rectangleShape, oobf, 1.0,
			Double.MAX_VALUE * 0.5);

		ops.threshold().localContrastThreshold(out, in, rectangleShape);
		ops.threshold().localContrastThreshold(out, in, rectangleShape, oobf);

		ops.threshold().localMedianThreshold(out, in, rectangleShape, 1.0);
		ops.threshold().localMedianThreshold(out, in, rectangleShape, oobf, 1.0);

		ops.threshold().localMidGreyThreshold(out, in, rectangleShape, 1.0);
		ops.threshold().localMidGreyThreshold(out, in, rectangleShape, oobf, 1.0);

		ops.threshold().localNiblackThreshold(out, in, rectangleShape, 1.0, 2.0);
		ops.threshold().localNiblackThreshold(out, in, rectangleShape, oobf, 1.0,
			2.0);

		ops.threshold().localPhansalkarThreshold(out, in, rectangleShape, oobf,
			0.25, 0.5);
		ops.threshold().localPhansalkarThreshold(out, in, rectangleShape);

		ops.threshold().localSauvolaThreshold(out, in, rectangleShape, oobf, 0.5,
			0.5);
		ops.threshold().localSauvolaThreshold(out, in, rectangleShape);

		/* Locally applied global threshold ops */
		ops.threshold().huang(out, in, rectangleShape, oobf);
		ops.threshold().huang(out, in, rectangleShape);

		ops.threshold().ij1(out, in, rectangleShape, oobf);
		ops.threshold().ij1(out, in, rectangleShape);

		ops.threshold().intermodes(out, in, rectangleShape, oobf);
		ops.threshold().intermodes(out, in, rectangleShape);

		ops.threshold().isoData(out, in, rectangleShape, oobf);
		ops.threshold().isoData(out, in, rectangleShape);

		ops.threshold().li(out, in, rectangleShape, oobf);
		ops.threshold().li(out, in, rectangleShape);

		ops.threshold().maxEntropy(out, in, rectangleShape, oobf);
		ops.threshold().maxEntropy(out, in, rectangleShape);

		ops.threshold().maxLikelihood(out, in, rectangleShape, oobf);
		ops.threshold().maxLikelihood(out, in, rectangleShape);

		ops.threshold().minError(out, in, rectangleShape, oobf);
		ops.threshold().minError(out, in, rectangleShape);

		ops.threshold().minimum(out, in, rectangleShape, oobf);
		ops.threshold().minimum(out, in, rectangleShape);

		ops.threshold().moments(out, in, rectangleShape, oobf);
		ops.threshold().moments(out, in, rectangleShape);

		ops.threshold().otsu(out, in, rectangleShape, oobf);
		ops.threshold().otsu(out, in, rectangleShape);

		ops.threshold().percentile(out, in, rectangleShape, oobf);
		ops.threshold().percentile(out, in, rectangleShape);

		ops.threshold().renyiEntropy(out, in, rectangleShape, oobf);
		ops.threshold().renyiEntropy(out, in, rectangleShape);

		ops.threshold().shanbhag(out, in, rectangleShape, oobf);
		ops.threshold().shanbhag(out, in, rectangleShape);

		ops.threshold().triangle(out, in, rectangleShape, oobf);
		ops.threshold().triangle(out, in, rectangleShape);

		ops.threshold().yen(out, in, rectangleShape, oobf);
		ops.threshold().yen(out, in, rectangleShape);
	}

	/**
	 * @see LocalBernsen
	 */
	@Test
	public void testLocalBernsenThreshold() {
		ops.run(LocalBernsen.class, out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			1.0, Double.MAX_VALUE * 0.5);

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see LocalContrast
	 */
	@Test
	public void testLocalContrastThreshold() {
		ops.run(LocalContrast.class, out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see LocalHuang
	 */
	@Test
	public void testLocalHuangThreshold() {
		ops.run(Huang.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see LocalIJ1
	 */
	@Test
	public void testLocalIJ1Threshold() {
		ops.run(IJ1.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see LocalIntermodes
	 */
	@Test
	public void testLocalIntermodesThreshold() {
		ops.run(Intermodes.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see LocalIsoData
	 */
	@Test
	public void testLocalIsoDataThreshold() {
		// NB: Test fails for RectangleShapes of span 1
		ops.run(IsoData.class, out, in, new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see LocalLi
	 */
	@Test
	public void testLocalLiThreshold() {
		ops.run(Li.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see LocalMaxEntropy
	 */
	@Test
	public void testLocalMaxEntropyThreshold() {
		ops.run(MaxEntropy.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see LocalMaxLikelihood
	 */
	@Test
	public void testLocalMaxLikelihoodThreshold() {
		// NB: Test fails for RectangleShapes of up to span==2
		ops.run(MaxLikelihood.class, out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see LocalMean
	 */
	@Test
	public void testLocalMeanThreshold() {
		ops.run(LocalMeanThreshold.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0);

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see IntegralLocalMean
	 */
	@Test
	public void testLocalMeanThresholdIntegral() {
		ops.run(IntegralLocalMean.class, out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0);

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see IntegralLocalMean
	 * @see LocalMeanThreshold
	 */
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
		ops.run(Ops.Threshold.LocalMeanThreshold.class, out2, in,
			new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0);

		// Integral image-based implementation
		ops.run(IntegralLocalMean.class, out3, in, new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0);

		assertIterationsEqual(out2, out3);
	}

	/**
	 * @see LocalMedian
	 */
	@Test
	public void testLocalMedianThreshold() {
		ops.run(LocalMedianThreshold.class, out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0);

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see LocalMidGrey
	 */
	@Test
	public void testLocalMidGreyThreshold() {
		ops.run(LocalMidGreyThreshold.class, out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0);

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see LocalThresholders.LocalMinimum
	 */
	@Test
	public void testLocalMinErrorThreshold() {
		ops.run(MinError.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see LocalThresholders.LocalMinimum
	 */
	@Test
	public void testLocalMinimumThreshold() {
		ops.run(Minimum.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see LocalThresholders.LocalMoments
	 */
	@Test
	public void testLocalMomentsThreshold() {
		ops.run(Moments.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see LocalNiblack
	 */
	@Test
	public void testLocalNiblackThreshold() {
		ops.run(LocalNiblack.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see IntegralLocalNiblack
	 */
	@Test
	public void testLocalNiblackThresholdIntegral() {
		ops.run(IntegralLocalNiblack.class, out, in, new RectangleShape(3, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);

		assertEquals(true, out.firstElement().get());
	}

	/**
	 * @see LocalNiblack
	 * @see IntegralLocalNiblack
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
		ops.run(LocalNiblack.class, out2, in, new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);

		// Integral image-based implementation
		ops.run(IntegralLocalNiblack.class, out3, in, new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);

		assertIterationsEqual(out2, out3);
	}

	/**
	 * @see LocalOtsu
	 */
	@Test
	public void testLocalOtsuThreshold() {
		ops.run(Otsu.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see LocalPercentile
	 */
	@Test
	public void testLocalPercentileThreshold() {
		ops.run(Percentile.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see LocalPhansalkar
	 */
	@Test
	public void testLocalPhansalkar() {
		ops.run(LocalPhansalkar.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see IntegralLocalPhansalkar
	 */
	@Test
	public void testLocalPhansalkarIntegral() {
		ops.run(IntegralLocalPhansalkar.class, out, in, new RectangleShape(3,
			false), null, 0.0, 0.0);

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see LocalPhansalkar
	 * @see IntegralLocalPhansalkar
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
		ops.run(LocalPhansalkar.class, out2, in, new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);

		// Integral image-based implementation
		ops.run(IntegralLocalPhansalkar.class, out3, in, new RectangleShape(2,
			false), null, 0.0, 0.0);

		assertIterationsEqual(out2, out3);
	}

	/**
	 * @see LocalRenyiEntropy
	 */
	@Test
	public void testLocalRenyiEntropyThreshold() {
		ops.run(RenyiEntropy.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see LocalSauvola
	 */
	@Test
	public void testLocalSauvola() {
		ops.run(LocalSauvola.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see IntegralLocalSauvola
	 */
	@Test
	public void testLocalSauvolaIntegral() {
		ops.run(IntegralLocalSauvola.class, out, in, new RectangleShape(3, false),
			null, 0.0, 0.0);

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see IntegralLocalSauvolaThresholdLearner
	 * @see LocalSauvola
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
		ops.run(LocalSauvola.class, out2, in, new RectangleShape(2, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE),
			0.0, 0.0);

		// Integral image-based implementation
		ops.run(IntegralLocalSauvola.class, out3, in, new RectangleShape(2, false),
			null, 0.0, 0.0);

		assertIterationsEqual(out2, out3);
	}

	/**
	 * @see LocalShanbhag
	 */
	@Test
	public void testLocalShanbhagThreshold() {
		ops.run(Shanbhag.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see LocalTriangle
	 */
	@Test
	public void testLocalTriangleThreshold() {
		ops.run(Triangle.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(false, out.firstElement().get());
	}

	/**
	 * @see LocalYen
	 */
	@Test
	public void testLocalYenThreshold() {
		ops.run(Yen.class, out, in, new RectangleShape(1, false),
			new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(Boundary.SINGLE));

		assertEquals(false, out.firstElement().get());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testContingencyOfNormalImplementation() {
		ops.run(LocalSauvolaThresholdLearner.class, out, in, new RectangleShape(3,
			false), new OutOfBoundsMirrorFactory<ByteType, Img<ByteType>>(
				Boundary.SINGLE), 0.0, 0.0);
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

}
