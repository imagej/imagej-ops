/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.loop;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.AbstractInplaceFunction;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imagej.ops.loop.LoopFunction;
import net.imagej.ops.loop.LoopInplace;
import net.imagej.ops.map.Map;
import net.imagej.ops.outputfactories.ImgImgSameTypeFactory;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing functional and inplace loops
 * 
 * @author Christian Dietz
 */
public class LoopTest extends AbstractOpTest {

	private Img<ByteType> in;
	private Img<ByteType> out;
	private Img<ByteType> buffer;

	private int numIterations;
	private Op functionalOp;
	private Op inplaceOp;

	@Before
	public void init() {
		final long[] dims = new long[] { 10, 10 };
		in = generateByteTestImg(false, dims);
		buffer = generateByteTestImg(false, dims);
		out = generateByteTestImg(false, dims);
		numIterations = 10;
		functionalOp = ops.op(Map.class, out, in, new AddOneFunctional());
		inplaceOp = ops.op(Map.class, in, new AddOneInplace());
	}

	@Test
	public void testInplace() {
		ops.run(LoopInplace.class, in, inplaceOp, numIterations);

		// test
		final Cursor<ByteType> c = in.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(numIterations, c.next().get());
		}
	}

	@Test
	public void testFunctionalEven() {
		ops.run(LoopFunction.class, out, in, functionalOp, new ImgImgSameTypeFactory<ByteType>(), numIterations);

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(numIterations, c.next().get());
		}
	}

	@Test
	public void testFunctionalOdd() {
		ops.run(LoopFunction.class, out, in, functionalOp, new ImgImgSameTypeFactory<ByteType>(),
			numIterations - 1);

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(numIterations - 1, c.next().get());
		}
	}

	// Helper classes
	class AddOneInplace extends AbstractInplaceFunction<ByteType> {

		@Override
		public ByteType compute(final ByteType arg) {
			arg.inc();
			return arg;
		}
	}

	class AddOneFunctional extends AbstractFunction<ByteType, ByteType> {

		@Override
		public ByteType compute(final ByteType input, final ByteType output) {
			output.set(input);
			output.inc();
			return output;
		}
	}
}
