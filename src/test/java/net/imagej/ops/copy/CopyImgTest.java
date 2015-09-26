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

import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.real.DoubleType;

import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link CopyImg}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class CopyImgTest extends AbstractOpTest {
	private Img<DoubleType> input;

	@Before
	public void createData() {
		input = new ArrayImgFactory<DoubleType>().create(
				new int[] { 120, 100 }, new DoubleType());

		final Random r = new Random(System.currentTimeMillis());

		final Cursor<DoubleType> inc = input.cursor();

		while (inc.hasNext()) {
			inc.next().set(r.nextDouble());
		}
	}

	@Test
	public void copyImgNoOutputTest() {
		@SuppressWarnings("unchecked")
		RandomAccessibleInterval<DoubleType> output = (RandomAccessibleInterval<DoubleType>) ops
				.run(CopyImg.class, input);

		Cursor<DoubleType> inc = input.localizingCursor();
		RandomAccess<DoubleType> outRA = output.randomAccess();

		while (inc.hasNext()) {
			inc.fwd();
			outRA.setPosition(inc);
			assertEquals(inc.get().get(), outRA.get().get(), 0.0);
		}
	}

	@Test
	public void copyImgWithOutputTest() {
		Img<DoubleType> output = input.factory().create(input,
				input.firstElement());

		ops.run(CopyImg.class, output, input);

		final Cursor<DoubleType> inc = input.cursor();
		final Cursor<DoubleType> outc = output.cursor();

		while (inc.hasNext()) {
			assertEquals(inc.next().get(), outc.next().get(), 0.0);
		}
	}
}
