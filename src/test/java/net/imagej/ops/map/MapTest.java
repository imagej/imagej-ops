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
import static org.junit.Assert.assertTrue;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.AbstractInplaceOp;
import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Christian Dietz (University of Konstanz)
 */
public class MapTest extends AbstractOpTest {

	private Img<ByteType> in;
	private Img<ByteType> out;
	private Img<ByteType> outDiffDims;

	@Before
	public void initImg() {
		in = generateByteTestImg(true, 10, 10);
		out = generateByteTestImg(false, 10, 10);
		outDiffDims = generateByteTestImg(false, 10, 10, 15);
	}

	@Test
	public void testMapIterableIntervalToIterableInterval() {
		final Op functional =
			ops.op(MapIterableIntervalToIterableInterval.class, out, in,
				new AddOneFunctional());
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
	public void testMapIterableIntervalToIterableIntervalDiffDims() {

		boolean fails = false;
		try {
			ops.run(MapIterableIntervalToIterableInterval.class, outDiffDims, in,
				new AddOneFunctional());
		}
		catch (final IllegalArgumentException e) {
			fails = true;
		}
		assertTrue(fails);
	}

	@Test
	public void testMapRAIToIterableInterval() {

		final Op functional =
			ops.op(MapRAIToIterableInterval.class, out, in, new AddOneFunctional());
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
	public void testMapRAIToIterableIntervalDiffDims() {
		boolean fails = false;
		try {
			ops.op(MapRAIToIterableInterval.class, outDiffDims, in,
				new AddOneFunctional());
		}
		catch (final IllegalArgumentException e) {
			fails = true;
		}
		assertTrue(fails);
	}

	@Test
	public void testMapIterableIntervalToRAI() {

		final Op functional =
			ops.op(MapIterableIntervalToRAI.class, out, in, new AddOneFunctional());
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
	public void testMapIterableIntervalToRAIDiffDims() {
		boolean fails = false;
		try {
			ops.op(MapIterableIntervalToRAI.class, outDiffDims, in,
				new AddOneFunctional());
		}
		catch (final IllegalArgumentException e) {
			fails = true;
		}
		assertTrue(fails);
	}

	@Test
	public void testMapIterableInplace() {

		final Cursor<ByteType> cursor1 = in.copy().cursor();
		final Cursor<ByteType> cursor2 = in.cursor();

		final Op functional =
			ops.op(MapIterableInplace.class, in, new AddOneInplace());
		functional.run();

		while (cursor1.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();
			assertEquals(cursor1.get().get() + 1, cursor2.get().get());
		}
	}

	@Test
	public void testMapIterableIntervalToView() {

		final Op functional =
			ops.op(MapIterableIntervalToView.class, in, new AddOneFunctional(),
				new ByteType());
		functional.run();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final IterableInterval<ByteType> o =
			(IterableInterval<ByteType>) ((MapIterableIntervalToView) functional)
				.out();

		final RandomAccess<ByteType> inputRA = in.randomAccess();
		final Cursor<ByteType> outCursor = o.localizingCursor();

		while (outCursor.hasNext()) {
			outCursor.fwd();
			inputRA.setPosition(outCursor);
			assertEquals(inputRA.get().get() + 1, outCursor.get().get());
		}
	}

	@Test
	public void testMapIterableToIterable() {

		final Op functional =
			ops.op(MapIterableToIterable.class, out, in, new AddOneFunctional());
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
	public void testMapConvertRAIToRAI() {

		final Op functional =
			ops.op(MapConvertRAIToRAI.class, in, new AddOneFunctional(),
				new ByteType());
		functional.run();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final RandomAccessibleInterval<ByteType> output =
			(RandomAccessibleInterval<ByteType>) ((MapConvertRAIToRAI) functional)
				.out();

		final Cursor<ByteType> inputC = in.cursor();
		final RandomAccess<ByteType> outputRA = output.randomAccess();

		while (inputC.hasNext()) {
			inputC.fwd();
			outputRA.setPosition(inputC);
			assertEquals(inputC.get().get() + 1, outputRA.get().get());
		}
	}

	@Test
	public void testMapConvertRandomAccessToRandomAccess() {

		final Op functional =
			ops.op(MapConvertRandomAccessToRandomAccess.class, in,
				new AddOneFunctional(), new ByteType());
		functional.run();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final RandomAccessible<ByteType> output =
			(RandomAccessible<ByteType>) ((MapConvertRandomAccessToRandomAccess) functional)
				.out();

		final Cursor<ByteType> inputC = in.cursor();
		final RandomAccess<ByteType> outputRA = output.randomAccess();

		while (inputC.hasNext()) {
			inputC.fwd();
			outputRA.setPosition(inputC);
			assertEquals(inputC.get().get() + 1, outputRA.get().get());
		}
	}

	// -- Helper classes --
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
