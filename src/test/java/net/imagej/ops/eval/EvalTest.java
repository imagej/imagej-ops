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

package net.imagej.ops.eval;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import net.imagej.ops.AbstractOpTest;

import org.junit.Test;

/**
 * Tests {@link net.imagej.ops.Ops.Eval}.
 * 
 * @author Curtis Rueden
 */
public class EvalTest extends AbstractOpTest {

	@Test
	public void testMath() {
		final Map<String, Object> vars = new HashMap<>();
		vars.put("a", 2);
		vars.put("b", 3);
		vars.put("c", 5);

		assertEquals(7, ops.run(DefaultEval.class, "a+c", vars));
		assertEquals(3, ops.run(DefaultEval.class, "c-a", vars));
		assertEquals(6, ops.run(DefaultEval.class, "a*b", vars));
		assertEquals(2, ops.run(DefaultEval.class, "c/a", vars));
		assertEquals(1, ops.run(DefaultEval.class, "c%a", vars));
		assertEquals(17, ops.run(DefaultEval.class, "a+b*c", vars));
	}

}
