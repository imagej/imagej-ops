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

import java.util.List;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric;
import net.imagej.ops.Ops.Geometric.BoundarySize;
import net.imagej.ops.Ops.Geometric.ConvexHull;
import net.imagej.ops.geom.helper.Polytope;
import net.imagej.ops.geom.helper.ThePolygon;
import net.imglib2.RealLocalizable;
import net.imglib2.type.numeric.real.DoubleType;

@Plugin(type = GeometricOp.class, label = "Geometric: ConvexHullPerimeter", name = Geometric.BoundarySizeConvexHull.NAME, priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultConvexHullBoundarySize
		extends
			AbstractFunctionOp<Polytope, DoubleType>
		implements
			Geometric.BoundarySizeConvexHull {

	private FunctionOp<List<RealLocalizable>, Polytope> convexHullFunc;

	private FunctionOp<Polytope, DoubleType> perimeterFunc;

	@Override
	public void initialize() {
		convexHullFunc = ops().function(ConvexHull.class, Polytope.class, in().getPoints());
		perimeterFunc = ops().function(BoundarySize.class, DoubleType.class, in());
	}

	@Override
	public DoubleType compute(Polytope input) {
		return perimeterFunc.compute(convexHullFunc.compute(input.getPoints()));
	}

}
