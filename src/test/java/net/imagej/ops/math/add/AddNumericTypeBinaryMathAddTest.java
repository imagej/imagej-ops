/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2024 ImageJ2 developers.
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

package net.imagej.ops.math.add;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.math.NumericTypeBinaryMath;
import net.imglib2.type.numeric.ARGBDoubleType;

import org.junit.Test;

/**
 * Tests {@link net.imagej.ops.math.NumericTypeBinaryMath.Add}.
 * 
 * @author Johannes Schindelin
 * @author Curtis Rueden
 */
public class AddNumericTypeBinaryMathAddTest extends AbstractOpTest {

	private final double DELTA = 0.00005;

	@Test
	public void testAdd() {
		final ARGBDoubleType a = new ARGBDoubleType(255, 128, 128, 128);
		final ARGBDoubleType b = new ARGBDoubleType(255, 75, 35, 45);
		final Op op = ops.op(Ops.Math.Add.class, a, a, b);
		assertSame(NumericTypeBinaryMath.Add.class, op.getClass());

		op.run();
		assertEquals(203.0, a.getR(), DELTA);
		assertEquals(163.0, a.getG(), DELTA);
		assertEquals(173.0, a.getB(), DELTA);
	}

}
