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

package net.imagej.ops.geom.geom2d;

import java.util.List;

import net.imagej.ops.Ops;
import net.imagej.ops.geom.GeomUtils;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.geom.real.Polygon2D;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.scijava.plugin.Plugin;

/**
 * Minimum Feret of a polygon.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
@Plugin(type = Ops.Geometric.MinimumFeret.class)
public class DefaultMinimumFeret extends
	AbstractUnaryFunctionOp<Polygon2D, Pair<RealLocalizable, RealLocalizable>>
	implements Ops.Geometric.MinimumFeret
{

	private UnaryFunctionOp<Polygon2D, Polygon2D> function;

	@Override
	public void initialize() {
		function = Functions.unary(ops(),
			Ops.Geometric.ConvexHull.class, Polygon2D.class, in());
	}

	@Override
	public Pair<RealLocalizable, RealLocalizable> calculate(Polygon2D input) {
		final List<? extends RealLocalizable> points = GeomUtils.vertices(function
			.calculate(input));

		double distance = Double.POSITIVE_INFINITY;
		RealLocalizable p0 = points.get(0);
		RealLocalizable p1 = points.get(0);
		double tmpDist = 0;
		RealLocalizable tmpP0 = p0;
		RealLocalizable tmpP1 = p1;

		for (int i = 0; i < points.size() - 2; i++) {
			final RealLocalizable lineStart = points.get(i);
			final RealLocalizable lineEnd = points.get(i + 1);
			tmpDist = 0;
			final Line l = new Line(new Vector2D(lineStart.getDoublePosition(0),
				lineStart.getDoublePosition(1)), new Vector2D(lineEnd.getDoublePosition(
					0), lineEnd.getDoublePosition(1)), 10e-12);

			for (int j = 0; j < points.size(); j++) {
				if (j != i && j != i + 1) {
					final RealLocalizable ttmpP0 = points.get(j);

					final double tmp = l.distance(new Vector2D(ttmpP0.getDoublePosition(
						0), ttmpP0.getDoublePosition(1)));

					if (tmp > tmpDist) {
						tmpDist = tmp;
						final Vector2D vp = (Vector2D) l.project(new Vector2D(ttmpP0
							.getDoublePosition(0), ttmpP0.getDoublePosition(1)));
						tmpP0 = new RealPoint(vp.getX(), vp.getY());
						tmpP1 = ttmpP0;
					}
				}
			}
			if (tmpDist < distance) {
				distance = tmpDist;
				p0 = tmpP0;
				p1 = tmpP1;
			}
		}

		final RealLocalizable lineStart = points.get(points.size() - 1);
		final RealLocalizable lineEnd = points.get(0);
		final Line l = new Line(new Vector2D(lineStart.getDoublePosition(0),
			lineStart.getDoublePosition(1)), new Vector2D(lineEnd.getDoublePosition(
				0), lineEnd.getDoublePosition(1)), 10e-12);
		tmpDist = 0;
		for (int j = 0; j < points.size(); j++) {
			if (j != points.size() - 1 && j != 0 + 1) {
				final RealLocalizable ttmpP0 = points.get(j);

				final double tmp = l.distance(new Vector2D(ttmpP0.getDoublePosition(0),
					ttmpP0.getDoublePosition(1)));

				if (tmp > tmpDist) {
					tmpDist = tmp;
					final Vector2D vp = (Vector2D) l.project(new Vector2D(ttmpP0
						.getDoublePosition(0), ttmpP0.getDoublePosition(1)));
					tmpP0 = new RealPoint(vp.getX(), vp.getY());
					tmpP1 = ttmpP0;
				}
			}
		}
		if (tmpDist < distance) {
			distance = tmpDist;
			p0 = tmpP0;
			p1 = tmpP1;
		}

		return new ValuePair<>(p0, p1);
	}
}
