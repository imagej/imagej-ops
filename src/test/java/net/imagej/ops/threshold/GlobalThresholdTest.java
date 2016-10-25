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

import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.threshold.manual.Manual;
import net.imglib2.IterableInterval;
import net.imglib2.img.Img;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link GlobalThresholder}s.
 * 
 * @author Stefan Helfrich (University of Konstanz)
 *
 */
public class GlobalThresholdTest extends AbstractThresholdTest {

	Img<BitType> out;

	/**
	 * Initialize images.
	 *
	 * @throws Exception
	 */
	@Before
	public void before() throws Exception {
		out = in.factory().imgFactory(new BitType()).create(in, new BitType());
	}

	/**
	 * Test whether parameters for ops in {@link ThresholdNamespace} opmethods are
	 * correctly set.
	 */
	@Test
	public void testOpMethods() {
		ops.threshold().huang(out, in);
		ops.threshold().huang(in);

		ops.threshold().ij1(out, in);
		ops.threshold().ij1(in);

		ops.threshold().intermodes(out, in);
		ops.threshold().intermodes(in);

		ops.threshold().isoData(out, in);
		ops.threshold().isoData(in);

		ops.threshold().li(out, in);
		ops.threshold().li(in);

		ops.threshold().maxEntropy(out, in);
		ops.threshold().maxEntropy(in);

		ops.threshold().maxLikelihood(out, in);
		ops.threshold().maxLikelihood(in);

		ops.threshold().minError(out, in);
		ops.threshold().minError(in);

		ops.threshold().minimum(out, in);
		ops.threshold().minimum(in);

		ops.threshold().moments(out, in);
		ops.threshold().moments(in);

		ops.threshold().otsu(out, in);
		ops.threshold().otsu(in);

		ops.threshold().percentile(out, in);
		ops.threshold().percentile(in);

		ops.threshold().renyiEntropy(out, in);
		ops.threshold().renyiEntropy(in);

		ops.threshold().shanbhag(out, in);
		ops.threshold().shanbhag(in);

		ops.threshold().triangle(out, in);
		ops.threshold().triangle(in);

		ops.threshold().yen(out, in);
		ops.threshold().yen(in);
	}

	/**
	 * Tests consistency of the MapView output (of a {@link UnaryFunctionOp}) and
	 * the output written by a {@link UnaryComputerOp}.
	 */
	@Test
	public void testFunctionOps() {
		ops.threshold().yen(out, in);
		IterableInterval<BitType> ii = ops.threshold().yen(in);

		assertIterationsEqual(out, ii);
	}
	
	/**
	 * Tests {@link Manual}.
	 * 
	 * @author Curtis Rueden
	 */
	@Test
	public void testManualGlobalThreshold() {
		final UnsignedShortType threshold = new UnsignedShortType(30000);
		ops.run(Manual.class, out, in, threshold);
		assertCount(out, 54);
	}

}
