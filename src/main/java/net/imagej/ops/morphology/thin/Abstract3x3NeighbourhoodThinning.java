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

import java.util.HashMap;

import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.type.logic.BitType;

/**
 * This abstract class represents an rough framework for a Thinning algorithm
 * operating on a 3x3 neighbourhood by providing some often needed functions.
 *
 * @author Andreas Burger, University of Konstanz
 */
public abstract class Abstract3x3NeighbourhoodThinning implements
	ThinningStrategy
{

	/**
	 * Boolean value of the foreground.
	 */
	protected boolean m_foreground;

	/**
	 * Boolean value of the background.
	 */
	protected boolean m_background;

	protected Abstract3x3NeighbourhoodThinning() {
		m_foreground = true;
		m_background = false;
	}

	/**
	 * Create a new abstract thinning strategy. The passed boolean will represent
	 * the foreground-value of the image.
	 *
	 * @param foreground Value determining the boolean value of foreground pixels.
	 */
	protected Abstract3x3NeighbourhoodThinning(final boolean foreground) {
		m_foreground = foreground;
		m_background = !foreground;
	}

	/**
	 * Returns all booleans in a 3x3 neighbourhood of the pixel the RandomAccess
	 * points to. These booleans are stored in an Array in the following order:
	 * <br>
	 * 8 1 2 <br>
	 * 7 0 3 <br>
	 * 6 5 4 <br>
	 *
	 * @param access A RandomAccess pointing to a pixel of the image
	 * @return A boolean Array holding the values of the neighbourhood in
	 *         clockwise order.
	 */
	protected boolean[] getNeighbourhood(final RandomAccess<BitType> access) {
		final boolean[] vals = new boolean[9];

		vals[0] = access.get().get();

		access.move(-1, 1);
		vals[1] = access.get().get();

		access.move(1, 0);
		vals[2] = access.get().get();

		access.move(1, 1);
		vals[3] = access.get().get();

		access.move(1, 1);
		vals[4] = access.get().get();

		access.move(-1, 0);
		vals[5] = access.get().get();

		access.move(-1, 0);
		vals[6] = access.get().get();

		access.move(-1, 1);
		vals[7] = access.get().get();

		access.move(-1, 1);
		vals[8] = access.get().get();

		return vals;

	}

	/**
	 * Returns the amount of switches from foreground to background occurring in
	 * the circle around vals[1]
	 *
	 * @param vals Boolean Array holding the neighbourhood.
	 * @return Amount of true-false switches in the neighbourhood.
	 */
	protected int findPatternSwitches(final boolean[] vals) {
		int res = 0;
		for (int i = 1; i < vals.length - 1; ++i) {
			if (vals[i] == m_foreground && vals[i + 1] == m_background) {
				++res;
			}
			if (vals[vals.length - 1] == m_foreground && vals[1] == m_background) {
				++res;
			}
		}
		return res;
	}

	@Override
	public void afterCycle() {
		// Intentionally left blank.
	}

	@Override
	public int getIterationsPerCycle() {
		return 1;
	}

	// TODO - Solve this in a less convoluted way.
	// We cache the RandomAccess instances for performance reasons.
	// But it would likely be even more performant if the strategy simply
	// accepted a RandomAccess directly in the first place, and the calling
	// code took care of reusing them appropriately.
	private HashMap<RandomAccessible, RandomAccess> accesses = new HashMap<>();
	protected <T> RandomAccess<T> randomAccess(RandomAccessible<T> ra) {
		return accesses.computeIfAbsent(ra, r -> r.randomAccess());
	}
}
