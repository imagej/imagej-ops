/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.Feret;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Feret}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Feret", name = Geometric2D.Feret.NAME)
public class DefaultFeret
		extends
			AbstractFunctionOp<Polygon, Pair<RealLocalizable, RealLocalizable>>
		implements
			GeometricOp<Polygon, Pair<RealLocalizable, RealLocalizable>>,
			Contingent,
			Geometric2D.Feret {

	@Override
	public Pair<RealLocalizable, RealLocalizable> compute(final Polygon input) {

		double distance = Double.NEGATIVE_INFINITY;
		int in0 = -1;
		int in1 = -1;

		for (int i = 0; i < input.getVertices().size(); i++) {
			for (int j = i + 1; j < input.getVertices().size(); j++) {
				RealLocalizable temp0 = input.getVertices().get(i);
				RealLocalizable temp1 = input.getVertices().get(j);

				double sum = 0;
				for (int k = 0; k < temp0.numDimensions(); k++) {
					sum += Math.pow(
							temp0.getDoublePosition(k)
									- temp1.getDoublePosition(k), 2);
				}
				sum = Math.sqrt(sum);

				if (sum > distance) {
					distance = sum;
					in0 = i;
					in1 = j;
				}
			}
		}

		return new ValuePair<RealLocalizable, RealLocalizable>(input
				.getVertices().get(in0), input.getVertices().get(in1));
	}

	@Override
	public boolean conforms() {
		return 2 == in().numDimensions();
	}

}
