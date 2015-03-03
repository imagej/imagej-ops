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
import net.imagej.ops.AbstractOpTest;

import org.junit.Test;

public class AndTest extends AbstractOpTest {

	@Test
	public void testAnd() {
		final Condition<?> c1 =
			ops.op(FunctionGreaterCondition.class, Double.class, 3.0);
		final Condition<?> c2 =
			ops.op(FunctionLesserCondition.class, Double.class, 6.0);

		final Boolean result = (Boolean) ops.run(AndCondition.class, 5.0, c1, c2);
		assertSame(result, true);

		final Boolean result2 = (Boolean) ops.run(AndCondition.class, 2.0, c1, c2);
		assertSame(result2, false);

		final Boolean result3 = (Boolean) ops.run(AndCondition.class, 7.0, c1, c2);
		assertSame(result3, false);

		final Boolean result4 =
			(Boolean) ops.run(AndCondition.class, Double.NaN, c1, c2);
		assertSame(result4, false);
	}
}
