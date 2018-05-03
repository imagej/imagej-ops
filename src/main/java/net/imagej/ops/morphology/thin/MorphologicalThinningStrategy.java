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

package net.imagej.ops.morphology.thin;

import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.type.logic.BitType;

import org.scijava.plugin.Plugin;

/**
 * A Class implementing a standard morphological thinning.
 *
 * @author Andreas Burger, University of Konstanz
 */
@Plugin(type = ThinningStrategy.class, name = "Morphological")
public class MorphologicalThinningStrategy extends
	Abstract3x3NeighbourhoodThinning
{

	public MorphologicalThinningStrategy() {
		super();
	}

	/**
	 * Create a new morphological thinning strategy. The passed boolean will
	 * represent the foreground-value of the image.
	 *
	 * @param foreground Value determining the boolean value of foreground pixels.
	 */
	public MorphologicalThinningStrategy(final boolean foreground) {
		super(foreground);
	}

	@Override
	public boolean removePixel(final long[] position,
		final RandomAccessible<BitType> accessible, final int iteration)
	{

		// Setup
		final RandomAccess<BitType> access = randomAccess(accessible);
		access.setPosition(position);

		final boolean[] vals = getNeighbourhood(access);

		// Depending on the current step of the cycle, we rotate the two Filters by
		// 0, 90, 180 or 270 Degrees.
		if (iteration % 4 == 0) {
			return top(vals);
		}

		if (iteration % 4 == 1) {
			return right(vals);
		}

		if (iteration % 4 == 2) {
			return bottom(vals);
		}

		if (iteration % 4 == 3) {
			return left(vals);
		}

		return false;

	}

	/*
	 * This method applies the filters without rotating. The actual filters are given by:
	 *
	 *  0   0   0           0   0
	 *      1           1   1   0
	 *  1   1   1           1
	 *
	 *  (Zero stands for background, 1 stands for foreground, no value represents a wildcard.)
	 *  Since the ThinningOp only checks pixels which are in the foreground, the center pixel is alway 1.
	 */
	private boolean top(final boolean[] vals) {
		if (vals[1] == m_background && vals[2] == m_background &&
			vals[8] == m_background && vals[4] == m_foreground &&
			vals[5] == m_foreground && vals[6] == m_foreground)
		{
			return true;
		}
		if (vals[1] == m_background && vals[2] == m_background &&
			vals[3] == m_background && vals[5] == m_foreground &&
			vals[7] == m_foreground)
		{
			return true;
		}

		return false;
	}

	// Rotated by 90 degrees RIGHT
	private boolean right(final boolean[] vals) {
		if (vals[2] == m_background && vals[3] == m_background &&
			vals[4] == m_background && vals[6] == m_foreground &&
			vals[7] == m_foreground && vals[8] == m_foreground)
		{
			return true;
		}
		if (vals[3] == m_background && vals[4] == m_background &&
			vals[5] == m_background && vals[1] == m_foreground &&
			vals[7] == m_foreground)
		{
			return true;
		}

		return false;
	}

	// Rotated by 180 degrees
	private boolean bottom(final boolean[] vals) {
		if (vals[4] == m_background && vals[5] == m_background &&
			vals[6] == m_background && vals[1] == m_foreground &&
			vals[2] == m_foreground && vals[8] == m_foreground)
		{
			return true;
		}
		if (vals[5] == m_background && vals[6] == m_background &&
			vals[7] == m_background && vals[1] == m_foreground &&
			vals[3] == m_foreground)
		{
			return true;
		}

		return false;
	}

	// Rotated by 270 degrees RIGHT or 90 degrees LEFT.
	private boolean left(final boolean[] vals) {
		if (vals[8] == m_background && vals[7] == m_background &&
			vals[6] == m_background && vals[2] == m_foreground &&
			vals[3] == m_foreground && vals[4] == m_foreground)
		{
			return true;
		}
		if (vals[7] == m_background && vals[8] == m_background &&
			vals[1] == m_background && vals[5] == m_foreground &&
			vals[3] == m_foreground)
		{
			return true;
		}

		return false;
	}

	@Override
	public int getIterationsPerCycle() {
		// To ensure correct order of filter applications and correct termination,
		// we need at least 4 iterations per cycle.
		// This guarantees that each filter is checked before terminating.
		return 4;
	}

	@Override
	public ThinningStrategy copy() {
		return new MorphologicalThinningStrategy(m_foreground);
	}

}
