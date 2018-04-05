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

package net.imagej.ops.image.integral;

import net.imglib2.AbstractEuclideanSpace;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhood;

/**
 * A cursor implementation that returns specific corner values of
 * {@link RectangleNeighborhood}s. The cursor returns, for example in 2D, the
 * values at the following positions:
 * <ul>
 * <li>(neighMin, neighMin),</li>
 * <li>(neighMax-1, neighMin),</li>
 * <li>(neighMax-1, neighMax-1), and</li>
 * <li>(neighMin, neighMax-1).</li>
 * </ul>
 * The mechanism naturally extends to nD. The current position can be obtained
 * from {@code getCornerRepresentation()} with 0s encoding (neighMin) and 1s
 * encoding (neighMax-1). The iteration order follows the (binary-reflected)
 * Gray code pattern such that only one dimension of the target position is
 * modified per move.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Gray_code">http://en.wikipedia.org
 *      /wiki/Gray_code</a>
 * @author Stefan Helfrich (University of Konstanz)
 */
public class IntegralCursor<T> extends AbstractEuclideanSpace implements
	Cursor<T>
{

	private int index = 0;

	/** Reflected binary code (Gray code) **/
	private int code = 0;

	private final int maxIndex;

	private final RandomAccess<T> source;

	private final RectangleNeighborhood<T> neighborhood;

	public IntegralCursor(final RectangleNeighborhood<T> neighborhood) {
		super(neighborhood.numDimensions());
		this.neighborhood = neighborhood;
		source = neighborhood.getSourceRandomAccess();
		maxIndex = ((int) Math.round(Math.pow(2, neighborhood.numDimensions()))) -
			1;
		reset();
	}

	protected IntegralCursor(final IntegralCursor<T> cursor) {
		super(cursor.numDimensions());
		neighborhood = cursor.neighborhood;
		source = cursor.source.copyRandomAccess();
		index = cursor.index;
		code = cursor.code;
		maxIndex = cursor.maxIndex;
	}

	@Override
	public void reset() {
		index = -1;
		code = 0;

		neighborhood.min(source);
	}

	@Override
	public void fwd() {
		// Check if cursor is uninitialized
		if (index == -1) {
			index++;
			return;
		}

		/*
		 * Adapted from Wikipedia:
		 *
		 * To construct the binary-reflected Gray code iteratively, at step 0
		 * start with the code = 0, and at each step index > 0 find the bit
		 * position of the least significant 1 in the binary representation of
		 * index and flip the bit at that position in the previous code to get
		 * the next code.
		 */
		index++;

		// Update Gray code
		final int mask = Integer.lowestOneBit(index);
		code ^= mask;

		// Move the cursor in the dimension of the updated bit
		final int updatedDimension = Integer.numberOfTrailingZeros(index);
		final int bitInDimension = (code & mask) >> updatedDimension;
		if (bitInDimension == 1) {
			source.setPosition(neighborhood.max(updatedDimension) - 1,
				updatedDimension);
		}
		else {
			source.setPosition(neighborhood.min(updatedDimension), updatedDimension);
		}
	}

	@Override
	public void jumpFwd(final long steps) {
		for (long i = 0; i < steps; ++i)
			fwd();
	}

	@Override
	public T get() {
		return source.get();
	}

	@Override
	public boolean hasNext() {
		return index < maxIndex;
	}

	@Override
	public T next() {
		fwd();
		return get();
	}

	@Override
	public IntegralCursor<T> copy() {
		return new IntegralCursor<>(this);
	}

	@Override
	public IntegralCursor<T> copyCursor() {
		return copy();
	}

	public int getCornerRepresentation() {
		return code;
	}

	@Override
	public void remove() {
		// NB: no action.
	}

	@Override
	public float getFloatPosition(final int d) {
		return source.getFloatPosition(d);
	}

	@Override
	public double getDoublePosition(final int d) {
		return source.getDoublePosition(d);
	}

	@Override
	public int getIntPosition(final int d) {
		return source.getIntPosition(d);
	}

	@Override
	public long getLongPosition(final int d) {
		return source.getLongPosition(d);
	}

	@Override
	public void localize(final long[] position) {
		source.localize(position);
	}

	@Override
	public void localize(final float[] position) {
		source.localize(position);
	}

	@Override
	public void localize(final double[] position) {
		source.localize(position);
	}

	@Override
	public void localize(final int[] position) {
		source.localize(position);
	}

}
