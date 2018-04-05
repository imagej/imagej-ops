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

package net.imagej.ops.morphology;

import java.util.List;

import net.imglib2.Interval;
import net.imglib2.algorithm.morphology.MorphologyUtils;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.type.logic.BitType;

/**
 * Utility class for morphology operations.
 * 
 * @author Leon Yang
 */
public class Morphologies {

	private Morphologies() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Computes the min coordinate and the size of an {@link Interval} after
	 * padding with a list of {@link Shape}s in a series morphology operations.
	 * 
	 * @param source the interval to be applied with some morphology operation
	 * @param shapes the list of Shapes for padding
	 * @return a size-2 array storing the min coordinate and the size of the
	 *         padded interval
	 */
	public static final long[][] computeMinSize(final Interval source,
		final List<Shape> shapes)
	{

		final int numDims = source.numDimensions();
		final long[] min = new long[numDims];
		final long[] size = new long[numDims];

		for (int i = 0; i < numDims; i++) {
			min[i] = source.min(i);
			size[i] = source.dimension(i);
		}

		for (final Shape shape : shapes) {
			final Neighborhood<BitType> nh = MorphologyUtils.getNeighborhood(shape,
				source);
			for (int i = 0; i < numDims; i++) {
				min[i] += nh.min(i);
				size[i] += nh.dimension(i) - 1;
			}
		}

		return new long[][] { min, size };
	}
}
