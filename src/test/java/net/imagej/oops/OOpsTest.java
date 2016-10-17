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

package net.imagej.oops;

import static org.junit.Assert.assertEquals;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;
import org.scijava.script.ScriptService;

/**
 * Tests for the optimized {@link Op}s.
 *
 * @author Johannes Schindelin
 */
public class OOpsTest {

	private OpService op;

	@Before
	public void setup() {
		op =
			new Context(OpService.class, ScriptService.class)
				.getService(OpService.class);
	}

	@Test
	public void testSimpleArithmetic() {
		assertEquals(3.0, OOps.eval(op, "X+Y", 1, 2));
		assertEquals(7.0, OOps.eval(op, "a+b*c", 1, 2, 3));
	}

	@Test
	public void testImageArithmetic() {

		final long[] dims = new long[] { 78, 23 };
		final Object blank = op.createimg(DoubleType.class, dims);

		// fill in the image with a sinusoid using a formula
		final String formula = "10 * (Math.cos(0.3*p[0]) + Math.sin(0.3*p[1]))";
		final Object sinusoid = op.equation(blank, formula);

		// add a constant value to an image
		op.add(sinusoid, 13.0);

		// generate a gradient image using a formula
		final Object gradient =
			op.equation(op.createimg(DoubleType.class, dims), "2+p[0]+p[1]");

		// add the two images
		final Object composite = op.createimg(DoubleType.class, dims);
		op.add(composite, sinusoid, gradient);

		final Object added = OOps.eval(op, "a+b", gradient, sinusoid);

		assertEquals(op.ascii(added), op.ascii(composite));

		// dump the image to the console
		// System.out.println(op.ascii(composite));
	}

}
