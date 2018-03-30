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

package net.imagej.ops.geom.geom3d;

import net.imagej.mesh.Mesh;
import net.imagej.mesh.Triangle;
import net.imagej.ops.Ops;
import net.imagej.ops.geom.GeometricOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.type.numeric.real.DoubleType;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link net.imagej.ops.Ops.Geometric.BoundarySize}.
 * 
 * @author Tim-Oliver Buchholz (University of Konstanz)
 */
@Plugin(type = Ops.Geometric.BoundarySize.class,
	label = "Geometric (3D): Surface Area",
	priority = Priority.VERY_HIGH)
public class DefaultSurfaceArea extends AbstractUnaryHybridCF<Mesh, DoubleType>
	implements GeometricOp<Mesh, DoubleType>, Ops.Geometric.BoundarySize
{

	@Override
	public void compute(final Mesh input, final DoubleType output) {
		double total = 0;
		for (final Triangle tri : input.triangles()) {
			final Vector3D v0 = new Vector3D(tri.v0x(), tri.v0y(), tri.v0z());
			final Vector3D v1 = new Vector3D(tri.v1x(), tri.v1y(), tri.v1z());
			final Vector3D v2 = new Vector3D(tri.v2x(), tri.v2y(), tri.v2z());

			final Vector3D cross = v0.subtract(v1).crossProduct(v2.subtract(v0));
			final double norm = cross.getNorm();
			if (norm > 0) total += norm * 0.5;
		}
		output.set(total);
	}
	
	@Override
	public DoubleType createOutput(Mesh input) {
		return new DoubleType();
	}

}
