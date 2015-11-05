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

import java.util.ArrayList;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.type.logic.BoolType;

import org.junit.Test;

/** Tests {@link UnionCondition}. */
public class UnionConditionTest extends AbstractOpTest {

	@Test
	public void test() {
		final ArrayList<Condition<Double>> condition =
			new ArrayList<Condition<Double>>();

		@SuppressWarnings("unchecked")
		final Condition<Double> c1 =
			ops.op(ComparableGreaterThan.class, Double.class, 3.0);
		@SuppressWarnings("unchecked")
		final Condition<Double> c2 =
			ops.op(ComparableGreaterThan.class, Double.class, 6.0);

		condition.add(c1);
		condition.add(c2);

		final BoolType result = ops.logic().or(2.0, condition);
		assertFalse(result.get());

		condition.add(0, c2);
		final BoolType result1 = ops.logic().or(4.0, condition);
		assertTrue(result1.get());

		final BoolType result2 = ops.logic().or(7.0, condition);
		assertTrue(result2.get());
	}

}
