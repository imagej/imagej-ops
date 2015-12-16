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

package net.imagej.ops.cached;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.OpInfo;
import net.imagej.ops.Ops;
import net.imagej.ops.special.AbstractUnaryHybridOp;
import net.imagej.ops.special.Functions;
import net.imagej.ops.special.Hybrids;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imagej.ops.special.UnaryHybridOp;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit-Tests for the {@link CachedOpEnvironment}.
 * 
 * Overriding with customOps is tested implicitly
 * 
 * @author Christian Dietz, University of Konstanz
 */
public class CachedOpEnvironmentTest extends AbstractOpTest {

	static int ctr = 0;

	private CachedOpEnvironment env;

	private Img<ByteType> imgA, imgB;

	private UnaryFunctionOp<Img<ByteType>, DoubleType> func;

	private UnaryHybridOp<Img<ByteType>, DoubleType> hybrid;

	@Before
	public void initCustomOps() {
		final ArrayList<OpInfo> customOps = new ArrayList<>();
		customOps.add(new OpInfo(MyMin.class));

		env = new CachedOpEnvironment(ops, customOps);

		imgA = generateByteArrayTestImg(true, new long[] { 10, 10 });
		imgB = generateByteArrayTestImg(true, new long[] { 10, 10 });

		func = Functions.unary(env, Ops.Stats.Min.class, DoubleType.class, imgA);
		hybrid = Hybrids.unary(env, Ops.Stats.Min.class, DoubleType.class, imgA);
	}

	@Test
	public void testCachingFunctionOp() {
		ctr = 0;

		// Calling it twice should result in the same result
		assertEquals(1.0, func.compute1(imgA).get(), 0.0);
		assertEquals(1.0, func.compute1(imgA).get(), 0.0);

		// Should be increased
		assertEquals(2.0, func.compute1(imgB).getRealDouble(), 0.0);
	}

	@Test
	public void testCachingHybrid() {
		ctr = 0;

		// Calling it twice should result in the same result
		assertEquals(1.0, hybrid.compute1(imgA).get(), 0.0);
		assertEquals(1.0, hybrid.compute1(imgA).get(), 0.0);

		// Should be increased
		assertEquals(2.0, hybrid.compute1(imgB).getRealDouble(), 0.0);
	}

	// some specialized ops to track number of counts
	public static class MyMin extends AbstractUnaryHybridOp<Img<ByteType>, DoubleType>
		implements Ops.Stats.Min
	{

		@Override
		public DoubleType createOutput(final Img<ByteType> input) {
			return new DoubleType();
		}

		@Override
		public void compute1(final Img<ByteType> input, final DoubleType output) {
			ctr++;
			output.set(ctr);
		}

	}

}
