/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.inplace.BinaryInplaceOp;
import net.imagej.ops.special.inplace.Inplaces;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link MapOp}s that are not covered in the auto generated tests.
 * 
 * @author Leon Yang
 * @author Christian Dietz (University of Konstanz)
 */
public class MapTest extends AbstractOpTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Op sub;

	@Test
	public void testIterable() {
		final Img<ByteType> in = generateByteArrayTestImg(true, 10, 10);

		final Op nullary = Computers.nullary(ops, Ops.Math.Zero.class,
			ByteType.class);
		ops.run(MapNullaryIterable.class, in, nullary);

		for (final ByteType ps : in)
			assertEquals(ps.get(), 0);
	}

	@Test
	public void testII() {
		final Img<ByteType> in = generateByteArrayTestImg(true, 10, 10);

		final Op nullary = Computers.nullary(ops, Ops.Math.Zero.class,
			ByteType.class);
		ops.run(MapNullaryII.class, in, nullary);

		for (final ByteType ps : in)
			assertEquals(ps.get(), 0);
	}

	@Test
	public void testIIAndIIInplace() {
		final Img<ByteType> first = generateByteArrayTestImg(true, 10, 10);
		final Img<ByteType> firstCopy = first.copy();
		final Img<ByteType> second = generateByteArrayTestImg(false, 10, 10);
		for (final ByteType px : second)
			px.set((byte) 1);
		final Img<ByteType> secondCopy = second.copy();
		final Img<ByteType> secondDiffDims = generateByteArrayTestImg(false, 10, 10,
			2);

		sub = Inplaces.binary(ops, Ops.Math.Subtract.class, ByteType.class);
		final BinaryInplaceOp<? super Img<ByteType>, Img<ByteType>> map = Inplaces
			.binary(ops, MapIIAndIIInplace.class, firstCopy, second, sub);
		map.run(firstCopy, second, firstCopy);
		map.run(first, secondCopy, secondCopy);

		assertImgSubEquals(first, second, firstCopy);
		assertImgSubEquals(first, second, secondCopy);

		// Expect exception when in2 has different dimensions
		thrown.expect(IllegalArgumentException.class);
		ops.op(MapIIAndIIInplace.class, first, secondDiffDims, sub);
	}

	@Test
	public void testIIAndIIInplaceParallel() {
		final Img<ByteType> first = generateByteArrayTestImg(true, 10, 10);
		final Img<ByteType> firstCopy = first.copy();
		final Img<ByteType> second = generateByteArrayTestImg(false, 10, 10);
		for (final ByteType px : second)
			px.set((byte) 1);
		final Img<ByteType> secondCopy = second.copy();
		final Img<ByteType> secondDiffDims = generateByteArrayTestImg(false, 10, 10,
			2);

		sub = Inplaces.binary(ops, Ops.Math.Subtract.class, ByteType.class);
		final BinaryInplaceOp<? super Img<ByteType>, Img<ByteType>> map = Inplaces
			.binary(ops, MapIIAndIIInplaceParallel.class, firstCopy, second, sub);
		map.run(firstCopy, second, firstCopy);
		map.run(first, secondCopy, secondCopy);

		assertImgSubEquals(first, second, firstCopy);
		assertImgSubEquals(first, second, secondCopy);

		// Expect exception when in2 has different dimensions
		thrown.expect(IllegalArgumentException.class);
		ops.op(MapIIAndIIInplaceParallel.class, first, secondDiffDims, sub);
	}

	@Test
	public void testIIInplaceParallel() {
		final Img<ByteType> arg = generateByteArrayTestImg(true, 10, 10);
		final Img<ByteType> argCopy = arg.copy();

		sub = Inplaces.unary(ops, Ops.Math.Subtract.class, ByteType.class,
			new ByteType((byte) 1));
		ops.run(MapIIInplaceParallel.class, argCopy, sub);

		assertImgSubOneEquals(arg, argCopy);
	}

	@Test
	public void testIterableInplace() {
		final Img<ByteType> arg = generateByteArrayTestImg(true, 10, 10);
		final Img<ByteType> argCopy = arg.copy();

		sub = Inplaces.unary(ops, Ops.Math.Subtract.class, ByteType.class,
			new ByteType((byte) 1));
		ops.run(MapIterableInplace.class, argCopy, sub);

		assertImgSubOneEquals(arg, argCopy);
	}

	@Test
	public void testIterableToIterable() {
		final Img<ByteType> in = generateByteArrayTestImg(true, 10, 10);
		final Img<ByteType> out = generateByteArrayTestImg(false, 10, 10);

		sub = Computers.unary(ops, Ops.Math.Subtract.class, ByteType.class,
			new ByteType((byte) 1));
		ops.run(MapIterableToIterable.class, out, in, sub);

		assertImgSubOneEquals(in, out);
	}

	// -- Test with CellImg --

	@Test
	public void testIICellImg() {
		final Img<ByteType> in = generateByteTestCellImg(true, 40, 20);

		final Op nullary = Computers.nullary(ops, Ops.Math.Zero.class,
			ByteType.class);
		ops.run(MapNullaryII.class, in, nullary);

		for (final ByteType ps : in)
			assertEquals(ps.get(), 0);
	}

	@Test
	public void testIIAndIIInplaceParallelCellImg() {
		final Img<ByteType> first = generateByteTestCellImg(true, 40, 20);
		final Img<ByteType> firstCopy = first.copy();
		final Img<ByteType> second = generateByteTestCellImg(false, 40, 20);
		for (final ByteType px : second)
			px.set((byte) 1);
		final Img<ByteType> secondCopy = second.copy();

		sub = Inplaces.binary(ops, Ops.Math.Subtract.class, ByteType.class);
		final BinaryInplaceOp<? super Img<ByteType>, Img<ByteType>> map = Inplaces
			.binary(ops, MapIIAndIIInplaceParallel.class, firstCopy, second, sub);
		map.run(firstCopy, second, firstCopy);
		map.run(first, secondCopy, secondCopy);

		assertImgSubEquals(first, second, firstCopy);
		assertImgSubEquals(first, second, secondCopy);
	}

	@Test
	public void testIIInplaceParallelCellImg() {
		final Img<ByteType> arg = generateByteTestCellImg(true, 40, 20);
		final Img<ByteType> argCopy = arg.copy();

		sub = Inplaces.unary(ops, Ops.Math.Subtract.class, ByteType.class,
			new ByteType((byte) 1));
		ops.run(MapIIInplaceParallel.class, argCopy, sub);

		assertImgSubOneEquals(arg, argCopy);
	}

	// -- helper methods --

	private static void assertImgSubEquals(final Img<ByteType> in1,
		final Img<ByteType> in2, final Img<ByteType> out)
	{
		final Cursor<ByteType> in1Cursor = in1.cursor();
		final Cursor<ByteType> in2Cursor = in2.cursor();
		final Cursor<ByteType> outCursor = out.cursor();

		while (in1Cursor.hasNext()) {
			assertEquals((byte) (in1Cursor.next().get() - in2Cursor.next().get()),
				outCursor.next().get());
		}
	}

	private static void assertImgSubOneEquals(final Img<ByteType> in,
		final Img<ByteType> out)
	{
		final Cursor<ByteType> in1Cursor = in.cursor();
		final Cursor<ByteType> outCursor = out.cursor();

		while (in1Cursor.hasNext()) {
			assertEquals((byte) (in1Cursor.next().get() - 1), outCursor.next().get());
		}
	}

}
