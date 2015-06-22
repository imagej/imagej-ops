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

package net.imagej.ops.arithmetic.real;

import static org.junit.Assert.assertEquals;
import net.imagej.ops.AbstractOpTest;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests {@link RealArccsc}.
 * 
 * @author Alison Walter
 * @author Curtis Rueden
 */
public class RealArccscTest extends AbstractOpTest {

	@Test
	public void testArccsc() {
		assertArccsc(-1, -Math.PI/2);
		assertArccsc(1, Math.PI/2);
		assertArccsc(2, Math.PI/6);
		assertArccsc(-2, -Math.PI/6);
		assertArccsc((2*Math.sqrt(3))/3, Math.PI/3);
		assertArccsc(-(2*Math.sqrt(3))/3, -Math.PI/3);
	}

	@Rule
	public ExpectedException exception = ExpectedException.none(); 
	
	@Test
	public void testIllegalArgument(){
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("arccsc(x) : x out of range");
		assertArccsc(0,0);
	}
	
	private void assertArccsc(double i, double o) {
		final DoubleType in = new DoubleType(i);
		final DoubleType out = ops.math().arccsc(in.createVariable(), in);
		assertEquals(o, out.get(), 1e-15);
	}

}
