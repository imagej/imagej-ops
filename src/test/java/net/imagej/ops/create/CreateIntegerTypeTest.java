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

package net.imagej.ops.create;

import static org.junit.Assert.assertEquals;

import net.imagej.ops.AbstractOpTest;
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
 * Tests several ways to create an IntegerType
 *
 * @author Christian Dietz (University of Konstanz)
 */

public class CreateIntegerTypeTest extends AbstractOpTest {

	@Test
	public void testCreateIntegerType() {

		assertEquals(ops.create().integerType(2).getClass(), BitType.class);
		assertEquals(ops.create().integerType(Byte.MAX_VALUE + 1)
			.getClass(), ByteType.class);
		assertEquals(ops.create().integerType((Byte.MAX_VALUE + 1) * 2)
			.getClass(), UnsignedByteType.class);
		assertEquals(ops.create().integerType((Short.MAX_VALUE + 1))
			.getClass(), ShortType.class);
		assertEquals(ops.create().integerType((Short.MAX_VALUE + 1) * 2)
			.getClass(), UnsignedShortType.class);
		assertEquals(ops.create().integerType((Integer.MAX_VALUE + 1))
			.getClass(), IntType.class);
		assertEquals(ops
			.create().integerType((Integer.MAX_VALUE + 1l) * 2l).getClass(),
			UnsignedIntType.class);
		assertEquals(ops.create().integerType(Long.MAX_VALUE).getClass(),
			LongType.class);

	}
}
