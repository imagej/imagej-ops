/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package imagej.ops.arithmetic.add;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import imagej.ops.AbstractOpTest;
import imagej.ops.Op;
import imagej.ops.OpService;
import net.imglib2.type.numeric.integer.ByteType;

import org.junit.Test;

/**
 * Tests {@link AddConstantToNumericType}.
 * 
 * @author Johannes Schindelin
 * @author Curtis Rueden
 */
public class AddConstantToNumericTypeTest extends AbstractOpTest {

	/**
	 * Verifies that {@link OpService#add(Object...)} finds the
	 * {@link AddConstantToNumericType}.
	 */
	@Test
	public void testAdd() {
		final ByteType a = new ByteType((byte) 17);
		final ByteType b = new ByteType((byte) 34);
		final Op op = ops.op("add", a, a, b);
		assertSame(AddConstantToNumericType.class, op.getClass());

		op.run();
		assertEquals((byte) 51, a.get());
	}

}
