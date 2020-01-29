/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
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

package net.imagej.ops.coloc.saca;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests {@link QNorm}.
 *
 * @author Curtis Rueden
 * @author Ellen T Arena
 */
public final class QNormTest {

	@Test
	public void testCompute() {
		assertEquals(-1.6448536279366273, QNorm.compute(0.05), 1e-4);
		assertEquals(-3.0902322467708747, QNorm.compute(0.001), 1e-4);
		assertEquals(-5.199368896310355, QNorm.compute(1e-7), 1e-4);
		assertEquals(Double.POSITIVE_INFINITY, QNorm.compute(1), 1e-4);
		assertEquals(Double.POSITIVE_INFINITY, QNorm.compute(0), 1e-4);
		assertEquals(2.3263478773566906, QNorm.compute(0.99), 1e-4);
		assertEquals(Double.NaN, QNorm.compute(-0.99), 1e-4);
		assertEquals(Double.NaN, QNorm.compute(1.1), 1e-4);
	}
}
