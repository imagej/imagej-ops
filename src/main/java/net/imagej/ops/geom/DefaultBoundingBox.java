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

import java.util.ArrayList;
import java.util.List;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops.Geometric;
import net.imagej.ops.Ops.Geometric.BoundingBox;
import net.imagej.ops.geom.helper.ThePolygon;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;

/**
 * Generic implementation of {@link BoundingBox}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Bounding Box", name = Geometric.BoundingBox.NAME)
public class DefaultBoundingBox extends AbstractFunctionOp<ThePolygon, ThePolygon>
		implements
			Geometric.BoundingBox {

	@Override
	public ThePolygon compute(final ThePolygon input) {
		double min_x = Double.POSITIVE_INFINITY;
		double max_x = Double.NEGATIVE_INFINITY;
		double min_y = Double.POSITIVE_INFINITY;
		double max_y = Double.NEGATIVE_INFINITY;

		for (RealLocalizable rl : input.getVertices()) {
			if (rl.getDoublePosition(0) < min_x) {
				min_x = rl.getDoublePosition(0);
			}
			if (rl.getDoublePosition(0) > max_x) {
				max_x = rl.getDoublePosition(0);
			}
			if (rl.getDoublePosition(1) < min_y) {
				min_y = rl.getDoublePosition(1);
			}
			if (rl.getDoublePosition(1) > max_y) {
				max_y = rl.getDoublePosition(1);
			}
		}

		List<RealLocalizable> bounds = new ArrayList<RealLocalizable>();
		bounds.add(new RealPoint(min_x, min_y));
		bounds.add(new RealPoint(min_x, max_y));
		bounds.add(new RealPoint(max_x, max_y));
		bounds.add(new RealPoint(max_x, min_y));
		return new ThePolygon(bounds);
	}

}
