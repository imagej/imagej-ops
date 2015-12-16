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

package net.imagej.ops.filter.gauss;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.Cursor;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.junit.Test;

/**
 * Tests Gaussian convolution.
 * 
 * @author Martin Horn (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 */
public class GaussTest extends AbstractOpTest {

	/** Tests the Gaussian. */
	@Test
	public void gaussRegressionTest() {

		final Img<ByteType> in = generateByteTestImg(true, new long[] { 10, 10 });
		final Img<ByteType> out1 =
			ops.create().img(in, Util.getTypeFromInterval(in));
		final double sigma = 5;
		final Img<ByteType> out2 =
			ops.create().img(in, Util.getTypeFromInterval(in));

		ops.filter().gauss(out1, in, sigma);
		try {
			Gauss3.gauss(sigma, Views.extendMirrorSingle(in), out2);
		}
		catch (IncompatibleTypeException e) {
			throw new RuntimeException(e);
		}

		// compare outputs
		final Cursor<ByteType> c1 = out1.cursor();
		final Cursor<ByteType> c2 = out2.cursor();

		while (c1.hasNext()) {
			org.junit.Assert.assertEquals(c1.next().getRealDouble(), c2.next()
				.getRealDouble(), 0);
		}
	}
}
