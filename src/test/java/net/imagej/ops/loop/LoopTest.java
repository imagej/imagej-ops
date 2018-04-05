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

package net.imagej.ops.loop;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.bufferfactories.ImgImgSameTypeFactory;
import net.imagej.ops.map.MapOp;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.inplace.AbstractUnaryInplaceOp;
import net.imagej.ops.special.inplace.Inplaces;
import net.imagej.ops.special.inplace.UnaryInplaceOp;
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
	private UnaryComputerOp<Img<ByteType>, Img<ByteType>> computerOp;
	private UnaryInplaceOp<? super Img<ByteType>, Img<ByteType>> inplaceOp;

	@Before
	public void init() {
		final long[] dims = new long[] { 10, 10 };
		in = generateByteArrayTestImg(false, dims);
		out = generateByteArrayTestImg(false, dims);
		numIterations = 10;
		computerOp = Computers.unary(ops, MapOp.class, out, in, new AddOneFunctional());
		inplaceOp = Inplaces.unary(ops, MapOp.class, in, new AddOneInplace());
	}

	@Test
	public void testInplace() {
		ops.run(DefaultLoopInplace.class, in, inplaceOp, numIterations);

		// test
		final Cursor<ByteType> c = in.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(numIterations, c.next().get());
		}
	}

	@Test
	public void testFunctionalEven() {
		ops.run(DefaultLoopComputer.class, out, in, computerOp,
			new ImgImgSameTypeFactory<ByteType>(), numIterations);

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(numIterations, c.next().get());
		}
	}

	@Test
	public void testFunctionalOdd() {
		ops.run(DefaultLoopComputer.class, out, in, computerOp,
			new ImgImgSameTypeFactory<ByteType>(), numIterations - 1);

		// test
		final Cursor<ByteType> c = out.cursor();

		while (c.hasNext()) {
			org.junit.Assert.assertEquals(numIterations - 1, c.next().get());
		}
	}

	// Helper classes
	class AddOneInplace extends AbstractUnaryInplaceOp<ByteType> {

		@Override
		public void mutate(final ByteType arg) {
			arg.inc();
		}
	}

	class AddOneFunctional extends AbstractUnaryComputerOp<ByteType, ByteType> {

		@Override
		public void compute(final ByteType input, final ByteType output) {
			output.set(input);
			output.inc();
		}
	}
}
