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
package net.imagej.ops.geometric;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.Centroid;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.geometric.Polygon;

import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Centroid}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Center of Gravity", name = Geometric2D.Centroid.NAME)
public class DefaultCenterOfGravity extends
		AbstractFunctionOp<Polygon, RealLocalizable> implements
		GeometricOp<Polygon, RealLocalizable>, Contingent, Geometric2D.Centroid {

	@Override
	public RealLocalizable compute(final Polygon input) {

		double area = 0;
		double cx = 0;
		double cy = 0;
		for (int i = 0; i < input.getVertices().size() - 1; i++) {
			RealLocalizable p0 = input.getVertices().get(i);
			RealLocalizable p1 = input.getVertices().get(i + 1);

			double p0_x = p0.getDoublePosition(0);
			double p0_y = p0.getDoublePosition(1);
			double p1_x = p1.getDoublePosition(0);
			double p1_y = p1.getDoublePosition(1);

			cx += (p0_x + p1_x) * (p0_x * p1_y - p1_x * p0_y);
			cy += (p0_y + p1_y) * (p0_x * p1_y - p1_x * p0_y);
		}

		return new RealPoint(cx / (area * 6), cy / (area * 6));
	}

	@Override
	public boolean conforms() {
		return 2 == in().numDimensions();
	}

}
