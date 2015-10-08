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

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric;
import net.imagej.ops.Ops.Geometric.Feret;
import net.imagej.ops.Ops.Geometric.FeretsAngle;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

/**
 * Generic implementation of {@link FeretsAngle}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Ferets Angle", name = Geometric.FeretsAngle.NAME)
public class DefaultFeretsAngle extends AbstractFunctionOp<Polygon, DoubleType>
		implements
		Geometric.FeretsAngle {

	@SuppressWarnings("rawtypes")
	private FunctionOp<Polygon, Pair> function;

	@Override
	public void initialize() {
		function = ops().function(Feret.class, Pair.class, in());
	}

	@SuppressWarnings("unchecked")
	@Override
	public DoubleType compute(final Polygon input) {
		double result;
		Pair<RealLocalizable, RealLocalizable> ferets = function.compute(input);

		RealLocalizable p1 = ferets.getA();
		RealLocalizable p2 = ferets.getB();

		if (p1.getDoublePosition(0) == p2.getDoublePosition(0)) {
			result = 90;
		}

		// tan alpha = opposite leg / adjacent leg
		// angle in radiants = atan(alpha)
		// angle in degree = atan(alpha) * (180/pi)
		final double opLeg = p2.getDoublePosition(1) - p1.getDoublePosition(1);
		final double adjLeg = p2.getDoublePosition(0) - p1.getDoublePosition(0);
		double degree = Math.atan((opLeg / adjLeg)) * (180.0 / Math.PI);
		if (adjLeg < 0) {
			degree = 180 - degree;
		}
		result = Math.abs(degree);
		return new DoubleType(result);
	}

}