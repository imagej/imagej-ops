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

package net.imagej.ops.map;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.AbstractInplaceOp;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing multi threaded implementation ({@link MapIterableToRAIParallel} and
 * {@link MapIterableToIterableParallel}) of the mappers. Assumption: Naive Implementation of
 * {@link MapIterableIntervalToRAI} works fine.
 * 
 * @author Christian Dietz (University of Konstanz)
 */
public class ThreadedMapTest extends AbstractOpTest {

	private Img<ByteType> in;
	private Img<ByteType> out;

	@Before
	public void initImg() {
		in = generateByteTestImg(true, 10, 10);
		out = generateByteTestImg(false, 10, 10);
	}

	@Test
	public void testMapII() {

		final Op functional =
			ops.op(MapIterableIntervalToIterableInterval.class, out, in, new AddOneFunctional());
		functional.run();

		final Cursor<ByteType> cursor1 = in.cursor();
		final Cursor<ByteType> cursor2 = out.cursor();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals(cursor1.get().get() + 1, cursor2.get().get());
		}
	}

	@Test
	public void testFunctionMapIIRAIP() {

		final Op functional =
			ops.op(MapIterableToRAIParallel.class, out, in, new AddOneFunctional());
		functional.run();

		final Cursor<ByteType> cursor1 = in.cursor();
		final Cursor<ByteType> cursor2 = out.cursor();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals(cursor1.get().get() + 1, cursor2.get().get());
		}

	}

	@Test
	public void testFunctionMapIIP() {

		final Op functional =
			ops.op(MapIterableToIterableParallel.class, out, in, new AddOneFunctional());
		functional.run();

		final Cursor<ByteType> cursor1 = in.cursor();
		final Cursor<ByteType> cursor2 = out.cursor();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals(cursor1.get().get() + 1, cursor2.get().get());
		}
	}

	@Test
	public void testInplaceMapP() {

		final Cursor<ByteType> cursor1 = in.copy().cursor();
		final Cursor<ByteType> cursor2 = in.cursor();

		final Op functional = ops.op(MapParallel.class, in, new AddOneInplace());
		functional.run();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals(cursor1.get().get() + 1, cursor2.get().get());
		}
	}

	// Helper classes
	private static class AddOneInplace extends AbstractInplaceOp<ByteType> {

		@Override
		public void compute(final ByteType arg) {
			arg.inc();
		}
	}

	private static class AddOneFunctional extends
		AbstractComputerOp<ByteType, ByteType>
	{

		@Override
		public void compute(final ByteType input, final ByteType output) {
			output.set(input);
			output.inc();
		}
	}

}
