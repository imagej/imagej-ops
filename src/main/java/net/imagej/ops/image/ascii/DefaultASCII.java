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

package net.imagej.ops.image.ascii;

import net.imagej.ops.AbstractOp;
import net.imagej.ops.Ops;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generates an ASCII version of an image.
 * <p>Only the first two dimensions of the image are considered.</p>
 * 
 * @author Curtis Rueden
 */
@Plugin(type = Ops.Image.ASCII.class, name = Ops.Image.ASCII.NAME)
public class DefaultASCII<T extends RealType<T>> extends AbstractOp implements
	Ops.Image.ASCII
{

	private static final String CHARS = " .,-+o*O#";

	@Parameter
	private IterableInterval<T> image;

	@Parameter(required = false)
	private T min;

	@Parameter(required = false)
	private T max;

	@Parameter(type = ItemIO.OUTPUT)
	private String ascii;

	@Override
	public void run() {
		if (min == null || max == null) {
			final Pair<T,T> minMax = ops().stats().minMax(image);
			if (min == null) min = minMax.getA();
			if (max == null) max = minMax.getB();
		}
		ascii = ascii(image, min, max);
	}

	// -- Utility methods --

	public static <T extends RealType<T>> String ascii(
		final IterableInterval<T> image, final T min, final T max)
	{
		final long dim0 = image.dimension(0);
		final long dim1 = image.dimension(1);
		// TODO: Check bounds.
		final int w = (int) (dim0 + 1);
		final int h = (int) dim1;

		// span = max - min
		final T span = max.copy();
		span.sub(min);

		// allocate ASCII character array
		final char[] c = new char[w * h];
		for (int y = 1; y <= h; y++) {
			c[w * y - 1] = '\n'; // end of row
		}

		// loop over all available positions
		final Cursor<T> cursor = image.localizingCursor();
		final int[] pos = new int[image.numDimensions()];
		final T tmp = image.firstElement().copy();
		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.localize(pos);
			final int index = w * pos[1] + pos[0];

			// normalized = (value - min) / (max - min)
			tmp.set(cursor.get());
			tmp.sub(min);
			tmp.div(span);

			final int charLen = CHARS.length();
			final int charIndex = (int) (charLen * tmp.getRealDouble());
			c[index] = CHARS.charAt(charIndex < charLen ? charIndex : charLen - 1);
		}

		return new String(c);
	}

}
