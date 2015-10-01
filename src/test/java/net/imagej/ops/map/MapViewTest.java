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

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imagej.ops.math.NumericTypeBinaryMath;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.view.Views;

import org.junit.Before;
import org.junit.Test;

public class MapViewTest extends AbstractOpTest {

	private Img<ByteType> in;

	private Op op;

	@Before
	public void init() {
		final long[] dims = new long[] { 10, 10 };
		in = generateByteTestImg(false, dims);
		op =
			ops.op(NumericTypeBinaryMath.Add.class, null, NumericType.class,
				new ByteType((byte) 10));
	}

	@Test
	public void testRandomAccessibleView() {
		@SuppressWarnings("unchecked")
		final RandomAccessible<ByteType> res =
			(RandomAccessible<ByteType>) ops.run(MapConvertRandomAccessToRandomAccess.class, in, op,
				new ByteType());

		final Cursor<ByteType> iterable =
			Views.iterable(Views.interval(res, in)).cursor();
		while (iterable.hasNext()) {
			assertEquals((byte) 10, iterable.next().get());
		}

	}

	@Test
	public void testRandomAccessibleIntervalView() {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<ByteType> res =
			(RandomAccessibleInterval<ByteType>) ops.run(MapConvertRAIToRAI.class, in, op,
				new ByteType());

		final Cursor<ByteType> iterable = Views.iterable(res).cursor();
		while (iterable.hasNext()) {
			assertEquals((byte) 10, iterable.next().get());
		}

	}

	@Test
	public void testIterableIntervalView() {
		@SuppressWarnings("unchecked")
		final IterableInterval<ByteType> res =
			(IterableInterval<ByteType>) ops.run(MapIterableIntervalToView.class, in, op,
				new ByteType());

		final Cursor<ByteType> iterable = res.cursor();
		while (iterable.hasNext()) {
			assertEquals((byte) 10, iterable.next().get());
		}
	}
}
