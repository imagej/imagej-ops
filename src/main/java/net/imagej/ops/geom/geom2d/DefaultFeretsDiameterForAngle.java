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
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.geom.real.Polygon2D;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Ferets Diameter for a certain angle.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
@Plugin(type = Ops.Geometric.FeretsDiameter.class)
public class DefaultFeretsDiameterForAngle extends
	AbstractUnaryHybridCF<Polygon2D, DoubleType> implements
	Ops.Geometric.FeretsDiameter
{

	@Parameter
	private double angle = 0;

	private UnaryFunctionOp<Polygon2D, Polygon2D> function;

	@Override
	public void initialize() {
		function = Functions.unary(ops(),
			Ops.Geometric.ConvexHull.class, Polygon2D.class, in());
	}

	@Override
	public void compute(Polygon2D input, DoubleType output) {
		final List<? extends RealLocalizable> points = GeomUtils.vertices(function
			.calculate(input));

		final double angleRad = -angle * Math.PI / 180.0;

		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;

		for (RealLocalizable p : points) {
			final double tmpX = p.getDoublePosition(0) * Math.cos(angleRad) - p
				.getDoublePosition(1) * Math.sin(angleRad);
			minX = tmpX < minX ? tmpX : minX;
			maxX = tmpX > maxX ? tmpX : maxX;
		}

		output.set(Math.abs(maxX - minX));
	}

	@Override
	public DoubleType createOutput(Polygon2D input) {
		return new DoubleType();
	}
}
