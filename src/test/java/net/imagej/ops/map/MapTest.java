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

package net.imagej.ops.map;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.inplace.AbstractUnaryInplaceOp;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Christian Dietz (University of Konstanz)
 * @author Leon Yang
 */
public class MapTest extends AbstractOpTest {

	private Img<ByteType> in;
	private Img<ByteType> out;
	private Img<ByteType> outDiffDims;

	@Before
	public void initImg() {
		in = generateByteArrayTestImg(true, 10, 10);
		out = generateByteArrayTestImg(false, 10, 10);
		outDiffDims = generateByteArrayTestImg(false, 10, 10, 15);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMapIterableIntervalToIterableInterval() {
		ops.run(MapIterableIntervalToIterableInterval.class, out, in,
			new AddOneFunctional());

		final Cursor<ByteType> cursor1 = in.cursor();
		final Cursor<ByteType> cursor2 = out.cursor();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals((byte) (cursor1.get().get() + 1), cursor2.get().get());
		}

		ops.op(MapIterableIntervalToIterableInterval.class, outDiffDims, in,
			new AddOneFunctional());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMapIterableIntervalToIterableIntervalParallel() {
		ops.run(MapIterableIntervalToIterableIntervalParallel.class, out, in,
			new AddOneFunctional());

		final Cursor<ByteType> cursor1 = in.cursor();
		final Cursor<ByteType> cursor2 = out.cursor();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals((byte) (cursor1.get().get() + 1), cursor2.get().get());
		}

		ops.run(MapIterableIntervalToIterableIntervalParallel.class, outDiffDims,
			in, new AddOneFunctional());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMapRAIToIterableInterval() {

		ops.run(MapRAIToIterableInterval.class, out, in, new AddOneFunctional());

		final Cursor<ByteType> cursor1 = in.cursor();
		final Cursor<ByteType> cursor2 = out.cursor();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals((byte) (cursor1.get().get() + 1), cursor2.get().get());
		}

		ops.op(MapRAIToIterableInterval.class, outDiffDims, in,
			new AddOneFunctional());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMapIterableIntervalToRAI() {

		ops.run(MapIterableIntervalToRAI.class, out, in, new AddOneFunctional());

		final Cursor<ByteType> cursor1 = in.cursor();
		final Cursor<ByteType> cursor2 = out.cursor();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals((byte) (cursor1.get().get() + 1), cursor2.get().get());
		}

		ops.op(MapIterableIntervalToRAI.class, outDiffDims, in,
			new AddOneFunctional());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMapIterableIntervalToRAIParallel() {

		ops.run(MapIterableIntervalToRAIParallel.class, out, in,
			new AddOneFunctional());

		final Cursor<ByteType> cursor1 = in.cursor();
		final Cursor<ByteType> cursor2 = out.cursor();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals((byte) (cursor1.get().get() + 1), cursor2.get().get());
		}

		ops.op(MapIterableIntervalToRAIParallel.class, outDiffDims, in,
			new AddOneFunctional());
	}

	@Test
	public void testMapIterableInplace() {

		final Cursor<ByteType> cursor1 = in.copy().cursor();
		final Cursor<ByteType> cursor2 = in.cursor();

		final Op functional = ops.op(MapIterableInplace.class, in,
			new AddOneInplace());
		functional.run();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals((byte) (cursor1.get().get() + 1), cursor2.get().get());
		}
	}

	@Test
	public void testMapIterableIntervalInplaceParallel() {

		final Cursor<ByteType> cursor1 = in.copy().cursor();
		final Cursor<ByteType> cursor2 = in.cursor();

		final Op functional = ops.op(MapIterableIntervalInplaceParallel.class, in,
			new AddOneInplace());
		functional.run();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals((byte) (cursor1.get().get() + 1), cursor2.get().get());
		}
	}

	@Test
	public void testMapIterableToIterable() {

		final Op functional = ops.op(MapIterableToIterable.class, out, in,
			new AddOneFunctional());
		functional.run();

		final Cursor<ByteType> cursor1 = in.cursor();
		final Cursor<ByteType> cursor2 = out.cursor();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals((byte) (cursor1.get().get() + 1), cursor2.get().get());
		}
	}

	// -- Helper classes --
	private static class AddOneInplace extends AbstractUnaryInplaceOp<ByteType> {

		@Override
		public void mutate(final ByteType arg) {
			arg.inc();
		}
	}

	private static class AddOneFunctional extends
		AbstractUnaryComputerOp<ByteType, ByteType>
	{

		@Override
		public void compute1(final ByteType input, final ByteType output) {
			output.set(input);
			output.inc();
		}
	}

}
