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
package net.imagej.ops.geometric3d;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;

import java.util.HashSet;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.scijava.plugin.Plugin;

@Plugin(type = Namespace.class)
public class Descriptor3DNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "descriptor3d";
	}

	@OpMethod(op = net.imagej.ops.geometric3d.BitTypeVertexInterpolator.class)
	public double[] VertexInterpolator(final int[] p1, final int[] p2, final double p1Value, final double p2Value) {
		final double[] result =
			(double[]) ops().run(net.imagej.ops.geometric3d.BitTypeVertexInterpolator.class, p1, p2, p1Value, p2Value);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geometric3d.DefaultCentroid.class)
	public <B extends BooleanType<B>> double[] Centroid(final IterableRegion<B> in) {
		final double[] result =
			(double[]) ops().run(net.imagej.ops.geometric3d.DefaultCentroid.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geometric3d.DefaultVertexInterpolator.class)
	public double[] VertexInterpolator(final int[] p1, final int[] p2, final double p1Value, final double p2Value, final double isolevel) {
		final double[] result =
			(double[]) ops().run(net.imagej.ops.geometric3d.DefaultVertexInterpolator.class, p1, p2, p1Value, p2Value, isolevel);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geometric3d.DefaultMarchingCubes.class)
	public <B extends BooleanType<B>> DefaultMesh MarchingCubes(final RandomAccessibleInterval<B> in) {
		final DefaultMesh result =
			(DefaultMesh) ops().run(net.imagej.ops.geometric3d.DefaultMarchingCubes.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultMarchingCubes.class)
	public <B extends BooleanType<B>> DefaultMesh MarchingCubes(final RandomAccessibleInterval<B> in, final double isolevel) {
		final DefaultMesh result =
			(DefaultMesh) ops().run(net.imagej.ops.geometric3d.DefaultMarchingCubes.class, in, isolevel);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultMarchingCubes.class)
	public <B extends BooleanType<B>> DefaultMesh MarchingCubes(final RandomAccessibleInterval<B> in, final double isolevel, final VertexInterpolator interpolatorClass) {
		final DefaultMesh result =
			(DefaultMesh) ops().run(net.imagej.ops.geometric3d.DefaultMarchingCubes.class, in, isolevel, interpolatorClass);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geometric3d.DefaultSecondMultiVariate3D.class)
	public <B extends BooleanType<B>> CovarianceOf2ndMultiVariate3D SecondMultiVariate3D(final IterableRegion<B> in) {
		final CovarianceOf2ndMultiVariate3D result =
			(CovarianceOf2ndMultiVariate3D) ops().run(net.imagej.ops.geometric3d.DefaultSecondMultiVariate3D.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geometric3d.QuickHull3DFromMC.class)
	public <B extends BooleanType<B>> Mesh convexhull3d(
			final IterableRegion<B> in) {
		final DefaultMesh result = (DefaultMesh) ops().run(
				net.imagej.ops.geometric3d.QuickHull3DFromMC.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultConvexHull3D.class)
	public Mesh convexhull3d(final HashSet<Vertex> in) {
		final DefaultMesh result = (DefaultMesh) ops().run(
				net.imagej.ops.geometric3d.DefaultConvexHull3D.class, in);
		return result;
	}
}
