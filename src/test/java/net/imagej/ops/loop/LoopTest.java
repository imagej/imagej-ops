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

package net.imagej.ops.loop;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.AbstractInplaceOp;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imagej.ops.bufferfactories.ImgImgSameTypeFactory;
import net.imagej.ops.map.MapOp;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing functional and inplace loops
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public class LoopTest extends AbstractOpTest {

	private Img<ByteType> in;
	private Img<ByteType> out;

	private int numIterations;
	private Op functionalOp;
	private Op inplaceOp;

	@Before
	public void init() {
		final long[] dims = new long[] { 10, 10 };
		in = generateByteArrayTestImg(false, dims);
		out = generateByteArrayTestImg(false, dims);
		numIterations = 10;
		functionalOp = ops.op(MapOp.class, out, in, new AddOneFunctional());
		inplaceOp = ops.op(MapOp.class, in, new AddOneInplace());
	}

	@Test
	public void testInplace() {
		ops.loop(in, inplaceOp, numIterations);

		// test
		final Cursor<ByteType> c = in.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(numIterations, c.next().get());
		}
	}

	@Test
	public void testFunctionalEven() {
		ops.loop(out, in, functionalOp, new ImgImgSameTypeFactory<ByteType>(), numIterations);

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(numIterations, c.next().get());
		}
	}

	@Test
	public void testFunctionalOdd() {
		ops.loop(out, in, functionalOp, new ImgImgSameTypeFactory<ByteType>(),
			numIterations - 1);

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(numIterations - 1, c.next().get());
		}
	}

	// Helper classes
	class AddOneInplace extends AbstractInplaceOp<ByteType> {

		@Override
		public void compute(final ByteType arg) {
			arg.inc();
		}
	}

	class AddOneFunctional extends AbstractComputerOp<ByteType, ByteType> {

		@Override
		public void compute(final ByteType input, final ByteType output) {
			output.set(input);
			output.inc();
		}
	}
}
