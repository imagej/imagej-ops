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
 * An implementation of the Algorithm proposed by C. J. Hilditch.
 *
 * @author Andreas Burger, University of Konstanz
 */
@Plugin(type = ThinningStrategy.class, name = "Hilditch")
public class HilditchThinningStrategy extends Abstract3x3NeighbourhoodThinning {

	public HilditchThinningStrategy() {
		super();
	}

	/**
	 * Create a new hilditch strategy. The passed boolean will represent the
	 * foreground-value of the image.
	 *
	 * @param foreground Value determining the boolean value of foreground pixels.
	 */
	public HilditchThinningStrategy(final boolean foreground) {
		super(foreground);
	}

	@Override
	public boolean removePixel(final long[] position,
		final RandomAccessible<BitType> accessible, final int iteration)
	{
		final RandomAccess<BitType> access = randomAccess(accessible);
		access.setPosition(position);

		final boolean[] vals = getNeighbourhood(access);

		// First condition is to ensure there are at least 2 and at most 6
		// neighbouring foreground pixels.
		int numForeground = 0;
		for (int i = 1; i < vals.length; ++i) {
			if (vals[i] == m_foreground) {
				++numForeground;
			}
		}

		if (!(2 <= numForeground && numForeground <= 6)) {
			return false;
		}

		// Second condition checks for transitions between foreground and
		// background. Exactly 1 such transition
		// is required.
		final int numPatterns = findPatternSwitches(vals);
		if (!(numPatterns == 1)) {
			return false;
		}

		// The third and fourth conditions require neighbourhoods of adjacent
		// pixels.

		// Access has to be reset to current image-position before moving it, since
		// the getNeighbourhood() method moves it to the top-left of the initial
		// pixel.
		access.setPosition(position);
		access.move(-1, 1);
		final int p2Patterns = findPatternSwitches((getNeighbourhood(access)));
		if (!((vals[1] == m_background || vals[3] == m_background ||
			vals[7] == m_background) || p2Patterns != 1))
		{
			return false;
		}

		access.setPosition(position);
		access.move(1, 0);
		final int p4Patterns = findPatternSwitches((getNeighbourhood(access)));

		if (!((vals[1] == m_background || vals[3] == m_background ||
			vals[5] == m_background) || p4Patterns != 1))
		{
			return false;
		}

		// If all conditions are met, we can safely remove the pixel.
		return true;
	}

	@Override
	public ThinningStrategy copy() {
		return new HilditchThinningStrategy(m_foreground);
	}

}
