/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.convert;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.imagej.ops.AbstractOpTest;
import net.imagej.ops.misc.MinMax;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.integer.ByteType;
import net.imglib2.type.numeric.integer.ShortType;

import org.junit.Before;
import org.junit.Test;

/**
 * A test for several convert functions.
 * 
 * @author Christian Dietz
 */
@SuppressWarnings("unchecked")
public class ConvertersTest extends AbstractOpTest {

	private Img<ByteType> in;
	private double inMin;
	private double inMax;

	@Before
	public void initalize() {
		in = generateByteTestImg(true, new long[] { 10, 10 });

		List<ByteType> minmax = (List<ByteType>) ops.run(MinMax.class, in);

		this.inMin = minmax.get(0).getRealDouble();
		this.inMax = minmax.get(1).getRealDouble();
	}

	/** The test. */
	@Test
	public void testConvertScale() throws IncompatibleTypeException {

		Img<ShortType> res = (Img<ShortType>) ops.run(ConvertScale.class, in,
				new ShortType());

		List<ShortType> minmax = (List<ShortType>) ops.run(MinMax.class, res);

		assertEquals((inMax - Byte.MIN_VALUE)
				/ (Byte.MAX_VALUE - Byte.MIN_VALUE), (minmax.get(1)
				.getRealDouble() - Short.MIN_VALUE)
				/ (Short.MAX_VALUE - Short.MIN_VALUE), 0);
	}

	/** The test. */
	@Test
	public void testConvertNormalizeScale() throws IncompatibleTypeException {

		Img<ShortType> res = (Img<ShortType>) ops.run(
				ConvertNormalizeScale.class, in, new ShortType());

		List<ShortType> minmax = (List<ShortType>) ops.run(MinMax.class, res);

		assertEquals(1.0, minmax.get(1).getRealDouble() / Short.MAX_VALUE, 0.0);
	}

	/** The test. */
	@Test
	public void testCopy() throws IncompatibleTypeException {

		Img<ShortType> res = (Img<ShortType>) ops.run(ConvertCopy.class, in,
				new ShortType());

		List<ShortType> minmax = (List<ShortType>) ops.run(MinMax.class, res);

		assertEquals(minmax.get(0).getRealDouble(), inMin, 0);
		assertEquals(minmax.get(1).getRealDouble(), inMax, 0);
	}
}
