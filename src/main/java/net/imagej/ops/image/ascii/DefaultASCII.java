/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2023 ImageJ2 developers.
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

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generates an ASCII version of an image.
 * <p>
 * Only the first two dimensions of the image are considered.
 * </p>
 *
 * @author Curtis Rueden
 */
@Plugin(type = Ops.Image.ASCII.class)
public class DefaultASCII<T extends RealType<T>> extends
	AbstractUnaryFunctionOp<IterableInterval<T>, String> implements
	Ops.Image.ASCII
{

	private static final String CHARS = "#O*o+-,. ";

	@Parameter(required = false)
	private T min;

	@Parameter(required = false)
	private T max;

	private UnaryFunctionOp<IterableInterval<T>, Pair<T, T>> minMaxFunc;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		minMaxFunc = (UnaryFunctionOp) Functions.unary(ops(),
			Ops.Stats.MinMax.class, Pair.class, in());
	}

	@Override
	public String calculate(final IterableInterval<T> input) {
		if (min == null || max == null) {
			final Pair<T, T> minMax = minMaxFunc.calculate(input);
			if (min == null) min = minMax.getA();
			if (max == null) max = minMax.getB();
		}

		DoubleType minSource = new DoubleType(min.getRealDouble());
		DoubleType maxSource = new DoubleType(max.getRealDouble());
		DoubleType minTarget = new DoubleType(0);
		DoubleType maxTarget = new DoubleType(CHARS.length());

		IterableInterval<DoubleType> converted = ops().convert().float64(input);
		IterableInterval<DoubleType> normalized = ops().image().normalize(converted,
			minSource, maxSource, minTarget, maxTarget);

		return ascii(normalized);
	}

	// -- Utility methods --

	public static String ascii(final IterableInterval<DoubleType> image) {
		final long dim0 = image.dimension(0);
		final long dim1 = image.dimension(1);

		final int w = (int) (dim0 + 1);
		final int h = (int) dim1;

		// allocate ASCII character array
		final char[] c = new char[w * h];
		for (int y = 1; y <= h; y++) {
			c[w * y - 1] = '\n'; // end of row
		}

		// loop over all available positions
		final Cursor<DoubleType> cursor = image.localizingCursor();
		final int[] pos = new int[image.numDimensions()];
		while (cursor.hasNext()) {
			cursor.fwd();
			cursor.localize(pos);
			final int index = w * pos[1] + pos[0];

			// grab the value from the normalized image, convert it to an ASCII char.
			// N.B. if the original value was at the max for the type range it will be
			// equal to the length of the char array after normalization. Thus to
			// prevent an exception when converting to ASCII we subtract one when the
			// normalized image value is equal to the length.
			int val = (int) cursor.get().getRealDouble();
			if (val == CHARS.length()) val--;
			c[index] = CHARS.charAt(val);
		}

		return new String(c);
	}

}
