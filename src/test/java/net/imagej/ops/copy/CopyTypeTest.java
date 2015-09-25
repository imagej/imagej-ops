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
package net.imagej.ops.copy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.imagej.ops.AbstractOpTest;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link CopyType}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class CopyTypeTest extends AbstractOpTest {

	private DoubleType dt;

	@Before
	public void createData() {
		dt = new DoubleType(2);
	}

	@Test
	public void copyTypeNoOutputTest() {
		Object out = ops.run(CopyType.class, dt);

		if (out instanceof DoubleType) {
			assertEquals(dt.get(), ((DoubleType) out).get(), 0.0);
		} else {
			assertTrue("Copy is not instance of DoubleType.", false);
		}

	}

	@Test
	public void copyTypeWithOutputTest() {
		DoubleType out = new DoubleType();
		ops.run(CopyType.class, out, dt);

		assertEquals(dt.get(), out.get(), 0.0);

	}
}
