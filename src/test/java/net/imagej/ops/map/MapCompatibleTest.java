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

package net.imagej.ops.map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for {@link Maps#compatible}.
 * 
 * @author Leon Yang
 */
public class MapCompatibleTest extends AbstractOpTest {

	private static IterableInterval<ByteType> in1II;
	private static IterableInterval<ByteType> in2II;
	private static IterableInterval<ByteType> outII;
	private static IterableInterval<ByteType> in1LargeII;
	private static IterableInterval<ByteType> in2LargeII;
	private static IterableInterval<ByteType> outLargeII;
	private static RandomAccessibleInterval<ByteType> in1RAI;
	private static RandomAccessibleInterval<ByteType> in2RAI;
	private static RandomAccessibleInterval<ByteType> outRAI;
	private static RandomAccessibleInterval<ByteType> in1LargeRAI;
	private static RandomAccessibleInterval<ByteType> in2LargeRAI;
	private static RandomAccessibleInterval<ByteType> outLargeRAI;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void init() {
		in1II = ArrayImgs.bytes(2, 2);
		in2II = ArrayImgs.bytes(2, 2);
		outII = ArrayImgs.bytes(2, 2);
		in1LargeII = ArrayImgs.bytes(3, 3);
		in2LargeII = ArrayImgs.bytes(3, 3);
		outLargeII = ArrayImgs.bytes(3, 3);
		in1RAI = (RandomAccessibleInterval<ByteType>) in1II;
		in2RAI = (RandomAccessibleInterval<ByteType>) in2II;
		outRAI = (RandomAccessibleInterval<ByteType>) outII;
		in1LargeRAI = (RandomAccessibleInterval<ByteType>) in1LargeII;
		in2LargeRAI = (RandomAccessibleInterval<ByteType>) in2LargeII;
		outLargeRAI = (RandomAccessibleInterval<ByteType>) outLargeII;
	}

	@Test
	public void testIInII() {
		assertTrue(Maps.compatible(in1II, outII));
		assertFalse(Maps.compatible(in1II, outLargeII));
	}

	@Test
	public void testIInRAI() {
		assertTrue(Maps.compatible(in1II, outRAI));

		// RAI contains II
		assertTrue(Maps.compatible(in1II, outLargeRAI));

		// RAI does not contain II
		assertFalse(Maps.compatible(in1LargeII, outRAI));
	}

	@Test
	public void testRAInII() {
		assertTrue(Maps.compatible(in1RAI, outII));

		// RAI contains II
		assertTrue(Maps.compatible(in1LargeRAI, outII));

		// RAI does not contain II
		assertFalse(Maps.compatible(in1RAI, outLargeII));
	}

	@Test
	public void testIInIInII() {
		assertTrue(Maps.compatible(in1II, in2II, outII));

		// IterationOrders do not match
		assertFalse(Maps.compatible(in1LargeII, in2II, outII));
		assertFalse(Maps.compatible(in1II, in2LargeII, outII));
		assertFalse(Maps.compatible(in1II, in2II, outLargeII));
	}

	@Test
	public void testIInIInRAI() {
		assertTrue(Maps.compatible(in1II, in2II, outRAI));

		// RAI contains II
		assertTrue(Maps.compatible(in1II, in2II, outLargeRAI));

		// IterationOrders do not match
		assertFalse(Maps.compatible(in1LargeII, in2II, outLargeRAI));

		// RAI does not contain II
		assertFalse(Maps.compatible(in1II, in2LargeII, outRAI));
	}

	@Test
	public void testIInRAInII() {
		assertTrue(Maps.compatible(in1II, in2RAI, outII));

		// RAI contains II
		assertTrue(Maps.compatible(in1II, in2LargeRAI, outII));

		// IterationOrders do not match
		assertFalse(Maps.compatible(in1LargeII, in2LargeRAI, outII));

		// RAI does not contain II
		assertFalse(Maps.compatible(in1II, in2RAI, outLargeII));
	}

	@Test
	public void testRAInIInII() {
		assertTrue(Maps.compatible(in1RAI, in2II, outII));

		// RAI contains II
		assertTrue(Maps.compatible(in1LargeRAI, in2II, outII));

		// IterationOrders do not match
		assertFalse(Maps.compatible(in1LargeRAI, in2LargeII, outII));

		// RAI does not contain II
		assertFalse(Maps.compatible(in1RAI, in2II, outLargeII));
	}

	@Test
	public void testIInRAInRAI() {
		assertTrue(Maps.compatible(in1II, in2RAI, outRAI));

		// RAI contains II
		assertTrue(Maps.compatible(in1II, in2LargeRAI, outLargeRAI));

		// RAI does not contain II
		assertFalse(Maps.compatible(in1LargeII, in2LargeRAI, outRAI));
	}

	@Test
	public void testRAInIInRAI() {
		assertTrue(Maps.compatible(in1RAI, in2II, outRAI));

		// RAI contains II
		assertTrue(Maps.compatible(in1LargeRAI, in2II, outLargeRAI));

		// RAI does not contain II
		assertFalse(Maps.compatible(in1LargeRAI, in2LargeII, outRAI));
	}

	@Test
	public void testRAInRAInII() {
		assertTrue(Maps.compatible(in1RAI, in2RAI, outII));

		// RAI contains II
		assertTrue(Maps.compatible(in1LargeRAI, in2LargeRAI, outII));

		// RAI does not contain II
		assertFalse(Maps.compatible(in1LargeRAI, in2RAI, outLargeII));
	}
}
