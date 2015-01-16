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

package net.imagej.ops.conditions;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;

import net.imagej.ops.AbstractOpTest;

import org.junit.Test;

public class UnionTest extends AbstractOpTest {

	@Test
	public void testIntersection() {

		final ArrayList<Condition<?>> condition = new ArrayList<Condition<?>>();

		final Condition<?> c1 = (Condition<?>) ops.op(
				FunctionGreaterCondition.class, Double.class, 3.0);
		final Condition<?> c2 = (Condition<?>) ops.op(
				FunctionGreaterCondition.class, Double.class, 6.0);

		condition.add(c1);
		condition.add(c2);

		final Boolean result = (Boolean) ops.run(UnionCondition.class, 2.0,
				condition);
		assertSame(result, false);

		condition.add(0, c2);
		final Boolean result1 = (Boolean) ops.run(UnionCondition.class, 4.0,
				condition);
		assertSame(result1, true);
		
		final Boolean result2 = (Boolean) ops.run(UnionCondition.class, 7.0,
				condition);
		assertSame(result2, true);
	}
}
