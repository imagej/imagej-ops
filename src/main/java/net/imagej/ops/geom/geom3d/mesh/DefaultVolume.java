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
package net.imagej.ops.geom.geom3d.mesh;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.AbstractUnaryFunctionOp;
import net.imglib2.type.numeric.real.DoubleType;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link net.imagej.ops.Ops.Geometric.Size}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Ops.Geometric.Size.class, label = "Geometric3D: Volume",
	priority = Priority.VERY_HIGH_PRIORITY-1)
public class DefaultVolume
		extends
			AbstractUnaryFunctionOp<Mesh, DoubleType>
		implements
			Ops.Geometric.Size,
			Contingent {

	@Override
	public DoubleType compute1(final Mesh input) {
		double volume = 0;
		for (Facet f : input.getFacets()) {
			TriangularFacet tf = (TriangularFacet) f;
			volume += signedVolumeOfTriangle(tf.getP0(), tf.getP1(), tf.getP2());	
		}
		return new DoubleType(Math.abs(volume));
	}

	private double signedVolumeOfTriangle(Vector3D p0, Vector3D p1, Vector3D p2) {
		 return p0.dotProduct(p1.crossProduct(p2)) / 6.0f;
	}
	
	@Override
	public boolean conforms() {
		return in().triangularFacets();
	}
}
