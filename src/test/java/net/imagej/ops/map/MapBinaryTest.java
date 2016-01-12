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
import net.imagej.ops.Ops;
import net.imagej.ops.special.BinaryComputerOp;
import net.imagej.ops.special.Computers;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Leon Yang
 */
public class MapBinaryTest extends AbstractOpTest {

	private Img<ByteType> in1;
	private Img<ByteType> in2;
	private Img<ByteType> out;
	private Img<ByteType> outDiffDims;
	private BinaryComputerOp<ByteType, ByteType, ByteType> add;

	@Before
	public void initImg() {
		in1 = generateByteArrayTestImg(true, 10, 10);
		in2 = generateByteArrayTestImg(false, 10, 10);
		for (ByteType px : in2)
			px.set((byte) 1);
		out = generateByteArrayTestImg(false, 10, 10);
		outDiffDims = generateByteArrayTestImg(false, 10, 10, 15);
		add = Computers.binary(ops, Ops.Math.Add.class, ByteType.class,
			ByteType.class, ByteType.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMapIIAndIIToII() {
		ops.run(MapIIAndIIToII.class, out, in1, in2, add);

		final Cursor<ByteType> in1Cursor = in1.cursor();
		final Cursor<ByteType> in2Cursor = in2.cursor();
		final Cursor<ByteType> outCursor = out.cursor();

		while (in1Cursor.hasNext()) {
			in1Cursor.fwd();
			in2Cursor.fwd();
			outCursor.fwd();
			assertEquals((byte) (in1Cursor.get().get() + in2Cursor.get().get()),
				outCursor.get().get());
		}

		ops.op(MapIIAndIIToII.class, outDiffDims, in1, in2, add);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMapIIAndIIToIIParallel() {
		ops.run(MapIIAndIIToIIParallel.class, out, in1, in2, add);

		final Cursor<ByteType> in1Cursor = in1.cursor();
		final Cursor<ByteType> in2Cursor = in2.cursor();
		final Cursor<ByteType> outCursor = out.cursor();

		while (in1Cursor.hasNext()) {
			in1Cursor.fwd();
			in2Cursor.fwd();
			outCursor.fwd();
			assertEquals((byte) (in1Cursor.get().get() + in2Cursor.get().get()),
				outCursor.get().get());
		}

		ops.op(MapIIAndIIToIIParallel.class, outDiffDims, in1, in2, add);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMapIIAndIIToRAI() {
		ops.run(MapIIAndIIToRAI.class, out, in1, in2, add);

		final Cursor<ByteType> in1Cursor = in1.cursor();
		final Cursor<ByteType> in2Cursor = in2.cursor();
		final Cursor<ByteType> outCursor = out.cursor();

		while (in1Cursor.hasNext()) {
			in1Cursor.fwd();
			in2Cursor.fwd();
			outCursor.fwd();
			assertEquals((byte) (in1Cursor.get().get() + in2Cursor.get().get()),
				outCursor.get().get());
		}

		ops.op(MapIIAndIIToRAI.class, outDiffDims, in1, in2, add);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMapIIAndIIToRAIParallel() {
		ops.run(MapIIAndIIToRAIParallel.class, out, in1, in2, add);

		final Cursor<ByteType> in1Cursor = in1.cursor();
		final Cursor<ByteType> in2Cursor = in2.cursor();
		final Cursor<ByteType> outCursor = out.cursor();

		while (in1Cursor.hasNext()) {
			in1Cursor.fwd();
			in2Cursor.fwd();
			outCursor.fwd();
			assertEquals((byte) (in1Cursor.get().get() + in2Cursor.get().get()),
				outCursor.get().get());
		}

		ops.op(MapIIAndIIToRAIParallel.class, outDiffDims, in1, in2, add);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMapIIAndRAIToRAI() {
		ops.run(MapIIAndRAIToRAI.class, out, in1, in2, add);

		final Cursor<ByteType> in1Cursor = in1.cursor();
		final Cursor<ByteType> in2Cursor = in2.cursor();
		final Cursor<ByteType> outCursor = out.cursor();

		while (in1Cursor.hasNext()) {
			in1Cursor.fwd();
			in2Cursor.fwd();
			outCursor.fwd();
			assertEquals((byte) (in1Cursor.get().get() + in2Cursor.get().get()),
				outCursor.get().get());
		}

		ops.op(MapIIAndRAIToRAI.class, outDiffDims, in1, in2, add);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMapIIAndRAIToRAIParallel() {
		ops.run(MapIIAndRAIToRAIParallel.class, out, in1, in2, add);

		final Cursor<ByteType> in1Cursor = in1.cursor();
		final Cursor<ByteType> in2Cursor = in2.cursor();
		final Cursor<ByteType> outCursor = out.cursor();

		while (in1Cursor.hasNext()) {
			in1Cursor.fwd();
			in2Cursor.fwd();
			outCursor.fwd();
			assertEquals((byte) (in1Cursor.get().get() + in2Cursor.get().get()),
				outCursor.get().get());
		}

		ops.op(MapIIAndRAIToRAIParallel.class, outDiffDims, in1, in2, add);
	}
}
