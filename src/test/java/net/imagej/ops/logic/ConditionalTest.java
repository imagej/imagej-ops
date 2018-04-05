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

package net.imagej.ops.logic;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.type.logic.BoolType;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Test;

/**
 * Tests {@link If} and {@link Default}.
 *
 * @author Leon Yang
 */
public class ConditionalTest extends AbstractOpTest {

	@Test
	public void testIf() {
		final ByteType ifTrueVal = new ByteType((byte) 10);
		final ByteType ifFalseVal = new ByteType((byte) 100);
		assertEquals(10, ((ByteType) ops.run(If.class, new BoolType(true),
			ifTrueVal, ifFalseVal)).get());
		assertEquals(100, ((ByteType) ops.run(If.class, new BoolType(false),
			ifTrueVal, ifFalseVal)).get());
	}

	@Test
	public void testDefault() {
		final ByteType out = new ByteType((byte) 10);
		final ByteType defaultVal = new ByteType((byte) 100);
		assertEquals(10, ((ByteType) ops.run(Default.class, out, new BoolType(true),
			defaultVal)).get());
		assertEquals(100, ((ByteType) ops.run(Default.class, out, new BoolType(
			false), defaultVal)).get());
	}

}
