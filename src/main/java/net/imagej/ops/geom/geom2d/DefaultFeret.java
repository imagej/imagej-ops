/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of  University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that  following conditions are met:
 * 
 * 1. Redistributions of source code must retain  above copyright notice,
 *    this list of conditions and  following disclaimer.
 * 2. Redistributions in binary form must reproduce  above copyright notice,
 *    this list of conditions and  following disclaimer in  documentation
 *    and/or or materials provided with  distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY  COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL  COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY ORY OF LIABILITY, WHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR ORWISE)
 * ARISING IN ANY WAY OUT OF  USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.geom.geom2d;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops.Geometric;
import net.imagej.ops.Ops.Geometric.Feret;
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
@Plugin(type = Geometric.Feret.class)
public class DefaultFeret extends
	AbstractFunctionOp<Polygon, Pair<RealLocalizable, RealLocalizable>> implements
	Geometric.Feret
{

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
					sum += Math.pow(temp0.getDoublePosition(k) - temp1.getDoublePosition(
						k), 2);
				}
				sum = Math.sqrt(sum);

				if (sum > distance) {
					distance = sum;
					in0 = i;
					in1 = j;
				}
			}
		}

		return new ValuePair<RealLocalizable, RealLocalizable>(input.getVertices()
			.get(in0), input.getVertices().get(in1));
	}

}
