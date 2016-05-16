/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.FinalDimensions;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.complex.ComplexDoubleType;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Test;

/**
 * Test {@link CopyType}.
 * 
 * @author Brian Northan
 */
public class CopyComplexTest extends AbstractOpTest {

	@Test
	public void copyRealToComplexTest() {
		FinalDimensions dims = new FinalDimensions(new long[] { 3, 3 });

		IterableInterval<DoubleType> imd = ops.create().img(dims, new DoubleType());

		double val = 0;

		for (DoubleType d : imd) {
			d.setReal(val);
			val = val + 1;
		}

		IterableInterval<ComplexDoubleType> imc = ops.create().img(dims,
			new ComplexDoubleType());

		UnaryComputerOp<DoubleType, ComplexDoubleType> copier = Computers.unary(ops,
			CopyComplex.class, new ComplexDoubleType(), new DoubleType());

		ops.map(imc, imd, copier);

		val = 0;
		for (ComplexDoubleType c : imc) {
			assertEquals(val, c.getRealDouble(), 0);
			assertEquals(0, c.getImaginaryDouble(), 0);
			val = val + 1;
		}

	}
}
