/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2024 ImageJ2 developers.
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
package net.imagej.ops.image.distancetransform;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.Test;
import org.scijava.util.MersenneTwisterFast;

/**
 * @author Simon Schmid (University of Konstanz)
 */ 
public class DistanceTransform2DTest extends AbstractOpTest {

	private static final double EPSILON = 0.0001;
	private static final long SEED = 0x12345678;

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		// create 2D image
		final RandomAccessibleInterval<BitType> in = ops.create().img(new FinalInterval(20, 20), new BitType());
		generate2DImg(in);

		/*
		 * test normal DT
		 */
		RandomAccessibleInterval<FloatType> out = (RandomAccessibleInterval<FloatType>) ops
				.run(DistanceTransform2D.class, null, in);
		compareResults(out, in, new double[] { 1, 1 });

		/*
		 * test calibrated DT
		 */
		final double[] calibration = new double[] { 2.54, 1.77 };
		out = (RandomAccessibleInterval<FloatType>) ops.run(DistanceTransform2DCalibration.class, null, in,
				calibration);
		compareResults(out, in, calibration);
	}

	/*
	 * generate a random BitType image
	 */
	private void generate2DImg(final RandomAccessibleInterval<BitType> in) {
		final RandomAccess<BitType> raIn = in.randomAccess();
		final MersenneTwisterFast random = new MersenneTwisterFast(SEED);

		for (int x = 0; x < in.dimension(0); x++) {
			for (int y = 0; y < in.dimension(1); y++) {
				raIn.setPosition(new int[] { x, y });
				raIn.get().set(random.nextBoolean());
			}
		}
	}

	/*
	 * "trivial" distance transform algorithm -> calculate distance to each
	 * pixel and select the shortest
	 */
	private void compareResults(final RandomAccessibleInterval<FloatType> out,
			final RandomAccessibleInterval<BitType> in, final double[] calibration) {
		final RandomAccess<FloatType> raOut = out.randomAccess();
		final RandomAccess<BitType> raIn = in.randomAccess();
		for (int x0 = 0; x0 < in.dimension(0); x0++) {
			for (int y0 = 0; y0 < in.dimension(1); y0++) {
				raIn.setPosition(new int[] { x0, y0 });
				raOut.setPosition(new int[] { x0, y0 });
				if (!raIn.get().get()) {
					assertEquals(0, raOut.get().get(), EPSILON);
				} else {
					double actualValue = in.dimension(0) * in.dimension(0) + in.dimension(1) * in.dimension(1);
					for (int x = 0; x < in.dimension(0); x++) {
						for (int y = 0; y < in.dimension(1); y++) {
							raIn.setPosition(new int[] { x, y });
							final double dist = calibration[0] * calibration[0] * (x0 - x) * (x0 - x)
									+ calibration[1] * calibration[1] * (y0 - y) * (y0 - y);
							if ((!raIn.get().get()) && (dist < actualValue))
								actualValue = dist;
						}
					}
					assertEquals(Math.sqrt(actualValue), raOut.get().get(), EPSILON);
				}
			}
		}
	}
}
