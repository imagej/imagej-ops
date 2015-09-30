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

import java.util.Set;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Namespace for geometric 3D features.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
@Plugin(type = Namespace.class)
public class Geometric3DNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "geometric3d";
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultCompactnessFeature.class)
	public <B extends BooleanType<B>> DoubleType Compactness(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geom.DefaultCompactnessFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultConvexHullSurfaceAreaFeature.class)
	public <B extends BooleanType<B>> DoubleType ConvexHullSurfaceArea(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops()
				.run(net.imagej.ops.geom.DefaultConvexHullSurfaceAreaFeature.class,
						in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultConvexHullSurfacePixelFeature.class)
	public <B extends BooleanType<B>> DoubleType ConvexHullSurfacePixel(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops()
				.run(net.imagej.ops.geom.DefaultConvexHullSurfacePixelFeature.class,
						in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultConvexHullVolumeFeature.class)
	public <B extends BooleanType<B>> DoubleType ConvexHullVolume(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops()
				.run(net.imagej.ops.geom.DefaultConvexHullVolumeFeature.class,
						in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultConvexityFeature.class)
	public <B extends BooleanType<B>> DoubleType Convexity(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geom.DefaultConvexityFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultMainElongationFeature.class)
	public <B extends BooleanType<B>> DoubleType MainElongation(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geom.DefaultMainElongationFeature.class,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultMedianElongationFeature.class)
	public <B extends BooleanType<B>> DoubleType MedianElongation(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops()
				.run(net.imagej.ops.geom.DefaultMedianElongationFeature.class,
						in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultRugosityFeature.class)
	public <B extends BooleanType<B>> DoubleType Rugosity(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geom.DefaultRugosityFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultSolidityFeature.class)
	public <B extends BooleanType<B>> DoubleType Solidity(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geom.DefaultSolidityFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultSparenessFeature.class)
	public <B extends BooleanType<B>> DoubleType Spareness(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geom.DefaultSparenessFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultSphericityFeature.class)
	public <B extends BooleanType<B>> DoubleType Sphericity(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geom.DefaultSphericityFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultSurfaceAreaFeature.class)
	public <B extends BooleanType<B>> DoubleType SurfaceArea(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geom.DefaultSurfaceAreaFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultSurfacePixelFeature.class)
	public <B extends BooleanType<B>> DoubleType SurfacePixel(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops()
				.run(net.imagej.ops.geom.DefaultSurfacePixelFeature.class,
						in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultVolumeFeature.class)
	public <B extends BooleanType<B>> DoubleType Volume(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geom.DefaultVolumeFeature.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.BitTypeVertexInterpolator.class)
	public double[] VertexInterpolator(final int[] p1, final int[] p2, final double p1Value, final double p2Value) {
		final double[] result =
			(double[]) ops().run(net.imagej.ops.geom.BitTypeVertexInterpolator.class, p1, p2, p1Value, p2Value);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultCentroid.class)
	public <B extends BooleanType<B>> double[] Centroid(final IterableRegion<B> in) {
		final double[] result =
			(double[]) ops().run(net.imagej.ops.geom.DefaultCentroid.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultVertexInterpolator.class)
	public double[] VertexInterpolator(final int[] p1, final int[] p2, final double p1Value, final double p2Value, final double isolevel) {
		final double[] result =
			(double[]) ops().run(net.imagej.ops.geom.DefaultVertexInterpolator.class, p1, p2, p1Value, p2Value, isolevel);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultMarchingCubes.class)
	public <B extends BooleanType<B>> DefaultMesh MarchingCubes(final RandomAccessibleInterval<B> in) {
		final DefaultMesh result =
			(DefaultMesh) ops().run(net.imagej.ops.geom.DefaultMarchingCubes.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultMarchingCubes.class)
	public <B extends BooleanType<B>> DefaultMesh MarchingCubes(final RandomAccessibleInterval<B> in, final double isolevel) {
		final DefaultMesh result =
			(DefaultMesh) ops().run(net.imagej.ops.geom.DefaultMarchingCubes.class, in, isolevel);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultMarchingCubes.class)
	public <B extends BooleanType<B>> DefaultMesh MarchingCubes(final RandomAccessibleInterval<B> in, final double isolevel, final VertexInterpolator interpolatorClass) {
		final DefaultMesh result =
			(DefaultMesh) ops().run(net.imagej.ops.geom.DefaultMarchingCubes.class, in, isolevel, interpolatorClass);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultSecondMultiVariate3D.class)
	public <B extends BooleanType<B>> CovarianceOf2ndMultiVariate3D SecondMultiVariate3D(final IterableRegion<B> in) {
		final CovarianceOf2ndMultiVariate3D result =
			(CovarianceOf2ndMultiVariate3D) ops().run(net.imagej.ops.geom.DefaultSecondMultiVariate3D.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultConvexHull3DFromMC.class)
	public <B extends BooleanType<B>> Mesh convexhull3d(
			final IterableRegion<B> in) {
		final DefaultMesh result = (DefaultMesh) ops().run(
				net.imagej.ops.geom.DefaultConvexHull3DFromMC.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultConvexHull3D.class)
	public Mesh convexhull3d(final HashSet<Vertex> in) {
		final DefaultMesh result = (DefaultMesh) ops().run(
				net.imagej.ops.geom.DefaultConvexHull3D.class, in);
		return result;
	}
}
