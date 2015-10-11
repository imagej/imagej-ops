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

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric;
import net.imagej.ops.Ops.Geometric.Feret;
import net.imagej.ops.Ops.Geometric.FeretsDiameter;
import net.imagej.ops.geom.GeometricOp;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

/**
 * Generic implementation of {@link FeretsDiameter}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric (2D): Ferets Diameter",
	name = Geometric.FeretsDiameter.NAME)
public class DefaultFeretsDiameter extends
	AbstractFunctionOp<Polygon, DoubleType> implements Geometric.FeretsDiameter
{

	private FunctionOp<Polygon, Pair<RealLocalizable, RealLocalizable>> function;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		function = (FunctionOp) ops().function(Feret.class, Pair.class, in());
	}

	@Override
	public DoubleType compute(final Polygon input) {
		Pair<RealLocalizable, RealLocalizable> ferets = function.compute(input);

		RealLocalizable p1 = ferets.getA();
		RealLocalizable p2 = ferets.getB();

		return new DoubleType(Math.hypot(p1.getDoublePosition(0) - p2
			.getDoublePosition(0), p1.getDoublePosition(1) - p2.getDoublePosition(
				1)));
	}

}
