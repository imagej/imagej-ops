/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2024 ImageJ2 developers.
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
package net.imagej.ops.geom;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.IterableRegion;

import org.scijava.plugin.Plugin;

/**
 * This {@link Op} computes the centroid of a {@link IterableRegion} (Label).
 * 
 * @author Tim-Oliver Buchholz (University of Konstanz)
 */
@Plugin(type = Ops.Geometric.Centroid.class, priority = 1)
public class CentroidII
		extends
			AbstractUnaryFunctionOp<IterableInterval<?>, RealLocalizable>
		implements
			Ops.Geometric.Centroid {

	@Override
	public RealLocalizable calculate(final IterableInterval<?> input) {
		int numDimensions = input.numDimensions();
		double[] output = new double[numDimensions];
		Cursor<?> c = input.localizingCursor();
		double[] pos = new double[numDimensions];
		while (c.hasNext()) {
			c.fwd();
			c.localize(pos);
			for (int i = 0; i < output.length; i++) {
				output[i] += pos[i];
			}
		}

		for (int i = 0; i < output.length; i++) {
			output[i] = output[i] / input.size();
		}

		return new RealPoint(output);
	}

}
