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

import net.imglib2.RandomAccessible;
import net.imglib2.type.logic.BitType;

import org.scijava.plugin.SingletonPlugin;

/**
 * An interface for a simple thinning strategy employed by the thinningOp.
 *
 * @author Andreas Burger, University of Konstanz
 */

public interface ThinningStrategy extends SingletonPlugin {

	/**
	 * This method should determine whether to keep a foreground pixel or not.
	 *
	 * @param position Long Array containing the current position in the image.
	 * @param accessible The image to thin.
	 * @param iteration The iteration number.
	 * @return True if pixel can be switched to background, false otherwise.
	 */
	public boolean removePixel(final long[] position,
		final RandomAccessible<BitType> accessible, int iteration);

	/**
	 * Returns the minimum number of iterations necessary for the algorithm to
	 * run. This delays termination of the thinning algorithm until the end of the
	 * current cycle. If, for example, no changes occur during the second
	 * iteration of a 4-iteration-cycle, iterations 3 and 4 still take place.
	 *
	 * @return The number of iterations per cycle.
	 */
	public int getIterationsPerCycle();

	/**
	 * Called by the ThinningOp after each cycle, and thus exactly
	 * getIterationsPerCycle()-times per iteration. Used for performing different
	 * calculations in each step of the cycle.
	 */
	public void afterCycle();

	/**
	 * Returns a seperate copy of this strategy.
	 *
	 * @return A new instance of this strategy with the same values.
	 */
	public ThinningStrategy copy();

}
