/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

package net.imagej.ops.transform.scaleView;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Test;

/**
 * Tests {@link DefaultScaleView}.
 * 
 * @author Martin Horn (University of Konstanz)
 * @author Stefan Helfrich (University of Konstanz)
 */
public class ScaleViewTest extends AbstractOpTest {

	@Test
	public void testScaling() {
		Img<ByteType> in = generateByteArrayTestImg(true, new long[] { 10, 10 });
		double[] scaleFactors = new double[] { 2, 2 };
		@SuppressWarnings("unchecked")
		RandomAccessibleInterval<ByteType> out = (RandomAccessibleInterval<ByteType>) ops.run(DefaultScaleView.class, in,
			scaleFactors, new NLinearInterpolatorFactory<ByteType>());

		assertEquals(out.dimension(0), 20);
		assertEquals(out.dimension(1), 20);

		RandomAccess<ByteType> inRA = in.randomAccess();
		RandomAccess<ByteType> outRA = out.randomAccess();
		inRA.setPosition(new long[] { 5, 5 });
		outRA.setPosition(new long[] { 10, 10 });
		assertEquals(inRA.get().get(), outRA.get().get());

	}

	@SuppressWarnings({ "unused", "unchecked" })
	@Test
	public void testOutOfBoundsFactoryIsNull() {
		Img<ByteType> in = generateByteArrayTestImg(true, new long[] { 10, 10 });
		double[] scaleFactors = new double[] { 2, 2 };
		NLinearInterpolatorFactory<ByteType> nLinearInterpolatorFactory =
			new NLinearInterpolatorFactory<ByteType>();
		RandomAccessibleInterval<ByteType> out = (RandomAccessibleInterval<ByteType>) ops.run(DefaultScaleView.class, in,
			scaleFactors, nLinearInterpolatorFactory, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testContingency() {
		Img<ByteType> in = generateByteArrayTestImg(true, new long[] { 10, 10 });
		double[] scaleFactors = new double[] { 2, 2, 2 };
		ops.run(DefaultScaleView.class, in, scaleFactors,
			new NLinearInterpolatorFactory<ByteType>());
	}
}
