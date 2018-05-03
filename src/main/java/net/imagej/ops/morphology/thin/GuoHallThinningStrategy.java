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
 * Represents the thinning algorithm proposed by Z. Guo and R. W. Hall.
 *
 * @author Andreas Burger, University of Konstanz
 */
@Plugin(type = ThinningStrategy.class, name = "GuoHall")
public class GuoHallThinningStrategy extends Abstract3x3NeighbourhoodThinning {

	public GuoHallThinningStrategy() {
		super();
	}

	/**
	 * Create a new Guo-Hall thinning strategy. The passed boolean will represent
	 * the foreground-value of the image.
	 *
	 * @param foreground Value determining the boolean value of foreground pixels.
	 */
	public GuoHallThinningStrategy(final boolean foreground) {
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

		// First we need to count the amount of connected neighbours in the
		// vicinity.
		int simpleConnectedNeighbours = 0;
		if (vals[1] == m_background && (vals[2] == m_foreground ||
			vals[3] == m_foreground))
		{
			++simpleConnectedNeighbours;
		}
		if (vals[3] == m_background && (vals[4] == m_foreground ||
			vals[5] == m_foreground))
		{
			++simpleConnectedNeighbours;
		}
		if (vals[5] == m_background && (vals[6] == m_foreground ||
			vals[7] == m_foreground))
		{
			++simpleConnectedNeighbours;
		}
		if (vals[7] == m_background && (vals[8] == m_foreground ||
			vals[1] == m_foreground))
		{
			++simpleConnectedNeighbours;
		}
		// First condition: Exactly one simple connected neighbour.
		if (simpleConnectedNeighbours != 1) {
			return false;
		}

		// Check for foreground pixels in each sector.
		final int sectorsOne = countSectors(vals, -1);
		final int sectorsTwo = countSectors(vals, 1);
		final int minSectors = Math.min(sectorsOne, sectorsTwo);
		if (!((2 <= minSectors) && (minSectors <= 3))) {
			return false;
		}

		boolean oddEvenCheck;

		// Depending on the step in the cycle, we need to perform different
		// calculations.
		if (iteration % 2 == 1) {
			oddEvenCheck = (vals[1] == m_foreground || vals[2] == m_foreground ||
				vals[4] == m_background) && vals[3] == m_foreground;
		}
		else {
			oddEvenCheck = (vals[5] == m_foreground || vals[6] == m_foreground ||
				vals[8] == m_background) && vals[7] == m_foreground;
		}
		if (oddEvenCheck) {
			return false;
		}
		return true;
	}

	// Count the foreground pixels in each of the four sectors. Depending on the
	// offset, the sector borders are
	// rotated by 45 degrees.
	private int countSectors(final boolean[] vals, final int offset) {
		int res = 0;
		for (int i = 1; i < vals.length - 1; i = i + 2) {
			if (i + offset == 0) {
				if (vals[1] == m_foreground || vals[8] == m_foreground) {
					++res;
				}
			}
			else if (vals[i] == m_foreground || vals[i + offset] == m_foreground) {
				++res;

			}
		}
		return res;
	}

	@Override
	public int getIterationsPerCycle() {
		return 2;

	}

	@Override
	public ThinningStrategy copy() {
		return new GuoHallThinningStrategy(m_foreground);
	}

}
