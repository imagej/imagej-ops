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

package net.imagej.ops.create;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.create.integerType.DefaultCreateIntegerType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.integer.ShortType;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedIntType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

import org.junit.Test;

/**
 * Tests several ways to create an IntegerType.
 *
 * @author Christian Dietz (University of Konstanz)
 */
public class CreateIntegerTypeTest extends AbstractOpTest {

	@Test
	public void testUint1() {
		assertNotType(BitType.class, 0L);
		assertType(BitType.class, 1L);
		assertNotType(BitType.class, 2L);
	}

	@Test
	public void testInt8() {
		assertType(ByteType.class, 0x7eL);
		assertType(ByteType.class, 0x7fL);
		assertNotType(ByteType.class, 0x80L);
	}

	@Test
	public void testUint8() {
		assertType(UnsignedByteType.class, 0xfeL);
		assertType(UnsignedByteType.class, 0xffL);
		assertNotType(UnsignedByteType.class, 0x100L);
	}

	@Test
	public void testInt16() {
		assertType(ShortType.class, 0x7ffeL);
		assertType(ShortType.class, 0x7fffL);
		assertNotType(ShortType.class, 0x8000L);
	}

	@Test
	public void testUint16() {
		assertType(UnsignedShortType.class, 0xfffeL);
		assertType(UnsignedShortType.class, 0xffffL);
		assertNotType(UnsignedShortType.class, 0x10000L);
	}

	@Test
	public void testInt32() {
		assertType(IntType.class, 0x7ffffffeL);
		assertType(IntType.class, 0x7fffffffL);
		assertNotType(IntType.class, 0x80000000L);
	}

	@Test
	public void testUint32() {
		assertType(UnsignedIntType.class, 0xfffffffeL);
		assertType(UnsignedIntType.class, 0xffffffffL);
		assertNotType(UnsignedIntType.class, 0x100000000L);
	}

	@Test
	public void testInt64() {
		assertType(LongType.class, 0x7ffffffffffffffeL);
		assertType(LongType.class, 0x7fffffffffffffffL);
	}

	// -- Helper methods --

	private void assertType(final Class<?> type, final long max) {
		assertEquals(type, ops.run(DefaultCreateIntegerType.class, max).getClass());
	}

	private void assertNotType(final Class<?> type, final long max) {
		assertNotEquals(type, ops.run(DefaultCreateIntegerType.class, max)
			.getClass());
	}

}
