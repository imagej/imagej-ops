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
 * Implementation of the thinning algorithm proposed by T. Y. Zhang and C. Y.
 * Suen.
 *
 * @author Andreas Burger, University of Konstanz
 */
@Plugin(type = ThinningStrategy.class, name = "ZhangSuen")
public class ZhangSuenThinningStrategy extends
	Abstract3x3NeighbourhoodThinning
{

	public ZhangSuenThinningStrategy() {
		super();
	}

	/**
	 * Create a new Zhang-Suen thinning strategy. The passed boolean will
	 * represent the foreground-value of the image.
	 *
	 * @param foreground Value determining the boolean value of foreground pixels.
	 */
	public ZhangSuenThinningStrategy(final boolean foreground) {
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

		// First two conditions are similar to Hilditch-Thinning.
		int numForeground = 0;

		for (int i = 1; i < vals.length; ++i) {
			if (vals[i] == m_foreground) {
				++numForeground;
			}
		}

		if (!(2 <= numForeground && numForeground <= 6)) {
			return false;
		}
		final int numPatternSwitches = findPatternSwitches(vals);
		if (!(numPatternSwitches == 1)) {
			return false;
		}

		// Currently, this thinning algorithm runs as 1-iteration-per-cycle, since
		// the order of operations is not important.
		if ((iteration % 2) != 1) {
			return evenIteration(vals);
		}
		return oddIteration(vals);

	}

	// Check for background pixels in the vicinity.
	private boolean evenIteration(final boolean[] vals) {
		if (!(vals[1] == m_background || vals[3] == m_background ||
			vals[5] == m_background))
		{
			return false;
		}

		if (!(vals[3] == m_background || vals[5] == m_background ||
			vals[7] == m_background))
		{
			return false;
		}

		return true;
	}

	// Variation of the checks in an even iteration.
	private boolean oddIteration(final boolean[] vals) {
		if (!(vals[1] == m_background || vals[3] == m_background ||
			vals[7] == m_background))
		{
			return false;
		}

		if (!(vals[1] == m_background || vals[5] == m_background ||
			vals[7] == m_background))
		{
			return false;
		}
		return true;
	}

	@Override
	public ThinningStrategy copy() {
		return new ZhangSuenThinningStrategy(m_foreground);
	}

}
