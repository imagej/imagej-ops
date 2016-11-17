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

package net.imagej.ops.pixml;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.pixml.DefaultHardClusterer;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.threshold.ThresholdLearner;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.img.Img;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link DefaultHardClusterer}.
 * 
 * @author Stefan Helfrich (University of Konstanz)
 */
public class DefaultHardClustererTest extends AbstractOpTest {

	Img<ByteType> in;
	Img<BitType> out;

	/**
	 * Initialize images.
	 *
	 * @throws Exception
	 */
	@Before
	public void before() throws Exception {
		in = generateByteArrayTestImg(true, new long[] { 3, 3 });
		out = in.factory().imgFactory(new BitType()).create(in, new BitType());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDefaultHardClusterer() {
		DumbUnsupervisedLearner<ByteType, BitType> dumbLearner =
			new DumbUnsupervisedLearner<>();
		IterableInterval<BitType> clustererOut = (IterableInterval<BitType>) ops
			.run(DefaultHardClusterer.class, in, dumbLearner,
				new BitType());

		// clustererOut is a MapView
		Cursor<BitType> cursor = clustererOut.cursor();
		while (cursor.hasNext()) {
			cursor.next();
		}
		assertEquals(9, DumbUnsupervisedLearner.counter);
	}

	private static class DumbUnsupervisedLearner<I extends RealType<I>, O extends BooleanType<O>>
		extends AbstractUnaryFunctionOp<IterableInterval<I>, UnaryComputerOp<I, O>>
		implements ThresholdLearner<I, O>
	{

		static long counter = 0;

		@Override
		public UnaryComputerOp<I, O> compute1(final IterableInterval<I> in) {
			return new AbstractUnaryComputerOp<I, O>() {

				@Override
				public void compute1(I input, O output) {
					counter++;
				}

			};
		}

	}

}
