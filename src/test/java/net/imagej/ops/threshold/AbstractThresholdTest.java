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

package net.imagej.ops.threshold;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import net.imagej.ops.AbstractOpTest;
import net.imglib2.RandomAccess;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.integer.UnsignedShortType;

import org.junit.Before;
import org.scijava.util.ListUtils;

/**
 * Tests for threshold ops.
 *
 * @author Brian Northan
 * @author Curtis Rueden
 */
public class AbstractThresholdTest extends AbstractOpTest {

	private final int xSize = 10;
	private final int ySize = 10;

	protected Img<UnsignedShortType> in;

	@Before
	public void initialize() {
		final long[] dimensions = new long[] { xSize, ySize };

		final Random r = new Random(0xdeadbeef);

		// create image and output
		in = ArrayImgs.unsignedShorts(dimensions);

		final RandomAccess<UnsignedShortType> ra = in.randomAccess();

		// populate pixel values with a ramp function + a constant
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				ra.setPosition(new int[] { x, y });
				ra.get().setReal(r.nextInt(65535));
			}
		}
	}

	protected Histogram1d<UnsignedShortType> histogram() {
		return ops.image().histogram(in);
	}

	protected void assertThreshold(final int expected, final Object actual) {
		final Object value =
			actual instanceof List ? ListUtils.first((List<?>) actual) : actual;
		assertTrue(value instanceof UnsignedShortType);
		final UnsignedShortType threshold = (UnsignedShortType) value;
		assertEquals(expected, threshold.get());
	}

	protected Img<BitType> bitmap() throws IncompatibleTypeException {
		return in.factory().imgFactory(new BitType()).create(in, new BitType());
	}

	/** Loops through the output pixels and count the number above zero. */
	protected void assertCount(final Img<BitType> out, final int expected) {
		long count = 0;
		for (final BitType b : out) {
			if (b.getRealFloat() > 0) {
				count++;
			}
		}
		assertEquals(expected, count);
	}

}
