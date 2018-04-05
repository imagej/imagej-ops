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

package net.imagej.ops.geom;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.geom.real.Polygon2D;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@code geom.centroid}.
 * 
 * @author Daniel Seebacher (University of Konstanz)
 */
@Plugin(type = Ops.Geometric.Centroid.class, label = "Geometric: Center of Gravity")
public class CentroidPolygon extends
	AbstractUnaryFunctionOp<Polygon2D, RealLocalizable> implements Ops.Geometric.Centroid
{

	private UnaryFunctionOp<Polygon2D, DoubleType> sizeFunc;

	@Override
	public void initialize() {
		sizeFunc = Functions.unary(ops(), Ops.Geometric.Size.class, DoubleType.class, in());
	}

	@Override
	public RealLocalizable calculate(final Polygon2D input) {

		double area = sizeFunc.calculate(input).get();
		double cx = 0;
		double cy = 0;
		for (int i = 0; i < input.numVertices(); i++) {
			RealLocalizable p0 = input.vertex(i);
			RealLocalizable p1 = input.vertex((i + 1) % input.numVertices());

			double p0_x = p0.getDoublePosition(0);
			double p0_y = p0.getDoublePosition(1);
			double p1_x = p1.getDoublePosition(0);
			double p1_y = p1.getDoublePosition(1);

			cx += (p0_x + p1_x) * (p0_x * p1_y - p1_x * p0_y);
			cy += (p0_y + p1_y) * (p0_x * p1_y - p1_x * p0_y);
		}

		return new RealPoint(cx / (area * 6), cy / (area * 6));
	}

}
