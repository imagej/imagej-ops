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

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.geom.geom3d.mesh.Mesh;
import net.imagej.ops.geom.geom3d.mesh.TriangularFacet;
import net.imagej.ops.special.AbstractUnaryFunctionOp;
import net.imagej.ops.special.Functions;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.real.DoubleType;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@code geom.centroid}.
 * 
 * Computation based on http://wwwf.imperial.ac.uk/~rn/centroid.pdf.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Ops.Geometric.Centroid.class, label = "Geometric: Centroid")
public class CentroidMesh extends AbstractUnaryFunctionOp<Mesh, RealLocalizable>
		implements
			Ops.Geometric.Centroid,
			Contingent {

	private UnaryFunctionOp<Mesh, DoubleType> sizeFunc;

	@Override
	public void initialize() {
		sizeFunc = Functions.unary(ops(), Ops.Geometric.Size.class, DoubleType.class, in());
	}

	@Override
	public RealLocalizable compute1(final Mesh input) {

		double c_x = 0;
		double c_y = 0;
		double c_z = 0;

		for (int i = 0; i < input.getFacets().size(); i++) {
			TriangularFacet f = (TriangularFacet) input.getFacets().get(i);
			Vector3D normal = f.getNormal();
			Vector3D a = f.getP0();
			Vector3D b = f.getP1();
			Vector3D c = f.getP2();
			c_x += (1 / 24d) * normal.getX() * (Math.pow((a.getX() + b.getX()), 2)
					+ Math.pow((b.getX() + c.getX()), 2)
					+ Math.pow((c.getX() + a.getX()), 2));
			c_y += (1 / 24d) * normal.getY() * (Math.pow((a.getY() + b.getY()), 2)
					+ Math.pow((b.getY() + c.getY()), 2)
					+ Math.pow((c.getY() + a.getY()), 2));
			c_z += (1 / 24d) * normal.getZ() * (Math.pow((a.getZ() + b.getZ()), 2)
					+ Math.pow((b.getZ() + c.getZ()), 2)
					+ Math.pow((c.getZ() + a.getZ()), 2));
		}

		double d = 1 / (2 * sizeFunc.compute1(input).get());
		c_x *= d;
		c_y *= d;
		c_z *= d;

		return new RealPoint(c_x, c_y, c_z);
	}

	@Override
	public boolean conforms() {
		return in().triangularFacets();
	}

}
