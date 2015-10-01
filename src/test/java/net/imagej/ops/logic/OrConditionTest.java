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

package net.imagej.ops.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.type.logic.BoolType;

import org.junit.Test;

/** Tests {@link OrCondition}. */
public class OrConditionTest extends AbstractOpTest {

	@Test
	public void test() {
		@SuppressWarnings("unchecked")
		final Condition<Object> c1 =
			ops.op(ComparableGreaterThan.class, Double.class, 3.0);
		@SuppressWarnings("unchecked")
		final Condition<Object> c2 =
			ops.op(ComparableLessThan.class, Double.class, 6.0);
		final Condition<Object> c3 =
			ops.op(ObjectsEqual.class, Double.class, 13.0);

		final BoolType result = ops.logic().or(5.0, c1, c2);
		assertTrue(result.get());

		final BoolType result2 = ops.logic().or(2.0, c1, c2);
		assertTrue(result2.get());

		final BoolType result3 = ops.logic().or(7.0, c1, c2);
		assertTrue(result3.get());

		final BoolType result4 = ops.logic().or(2.0, c1, c3);
		assertFalse(result4.get());
	}

}
