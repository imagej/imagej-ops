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

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.geom.helper.CovarianceOf2ndMultiVariate3D;
import net.imagej.ops.geom.helper.Mesh;
import net.imagej.ops.geom.helper.Polytope;
import net.imagej.ops.geom.helper.ThePolygon;
import net.imagej.ops.geom.helper.Vertex;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

/**
 * Namespace for Geom.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
@Plugin(type = Namespace.class)
public class GeomNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "geom";
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultSurfacePixelFeature.class)
	public DoubleType boundarypixel(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultSurfacePixelFeature.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultConvexHullSurfacePixelFeature.class)
	public DoubleType boundarypixelconvexhull(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultConvexHullSurfacePixelFeature.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultSurfaceAreaFeature.class)
	public DoubleType boundarysize(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultSurfaceAreaFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultPerimeter.class)
	public DoubleType boundarysize(final ThePolygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultPerimeter.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultConvexHullBoundarySize.class)
	public DoubleType boundarysizeconvexhull(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultConvexHullBoundarySize.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultBoundingboxivity.class)
	public DoubleType boundingboxivity(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultBoundingboxivity.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.CentroidFromIterableRegion.class)
	public <B extends BooleanType<B>> RealLocalizable centroid(final IterableRegion<B> in) {
		final RealLocalizable result =
			(RealLocalizable) ops().run(net.imagej.ops.geom.CentroidFromIterableRegion.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.CentroidFromPolygon.class)
	public RealLocalizable centroid(final ThePolygon in) {
		final RealLocalizable result =
			(RealLocalizable) ops().run(net.imagej.ops.geom.CentroidFromPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.CentroidFromMesh.class)
	public RealLocalizable centroid(final Mesh in) {
		final RealLocalizable result =
			(RealLocalizable) ops().run(net.imagej.ops.geom.CentroidFromMesh.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultCircularity.class)
	public DoubleType circularity(final ThePolygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultCircularity.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultCompactnessFeature.class)
	public DoubleType compactness(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultCompactnessFeature.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultContour.class)
	public <T extends Type<T>> ThePolygon contour(final RandomAccessibleInterval<T> in, final boolean useJacobs, final boolean isInverted) {
		final ThePolygon result =
			(ThePolygon) ops().run(net.imagej.ops.geom.DefaultContour.class, in, useJacobs, isInverted);
		return result;
	}

	@OpMethod(ops={DefaultConvexHull3D.class, DefaultConvexHull.class})
	public Polytope convexhull(final List<RealLocalizable> in) {
		final Polytope result =
			(Polytope) ops().run(net.imagej.ops.Ops.Geometric.ConvexHull.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultConvexityFeature.class)
	public DoubleType convexity(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultConvexityFeature.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultEccentricity.class)
	public DoubleType eccentricity(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultEccentricity.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultFeret.class)
	public Pair<RealLocalizable, RealLocalizable> feret(final Polytope in) {
		final Pair<RealLocalizable, RealLocalizable> result =
			(Pair<RealLocalizable, RealLocalizable>) ops().run(net.imagej.ops.geom.DefaultFeret.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultFeretsAngle.class)
	public DoubleType feretsangle(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultFeretsAngle.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultFeretsDiameter.class)
	public DoubleType feretsdiameter(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultFeretsDiameter.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultMainElongationFeature.class)
	public DoubleType mainelongation(final IterableRegion in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultMainElongationFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultElongation.class)
	public DoubleType mainelongation(final ThePolygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultElongation.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultMajorAxis.class)
	public DoubleType majoraxis(final ThePolygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultMajorAxis.class, in);
		return result;
	}
	

	@OpMethod(op = net.imagej.ops.geom.DefaultMarchingCubes.class)
	public <T extends Type<T>> Mesh marchingcubes(final RandomAccessibleInterval<T> in) {
		final Mesh result =
			(Mesh) ops().run(net.imagej.ops.geom.DefaultMarchingCubes.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultMarchingCubes.class)
	public<T extends Type<T>>  Mesh marchingcubes(final RandomAccessibleInterval<T> in, final double isolevel) {
		final Mesh result =
			(Mesh) ops().run(net.imagej.ops.geom.DefaultMarchingCubes.class, in, isolevel);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultMarchingCubes.class)
	public<T extends Type<T>>  Mesh marchingcubes(final RandomAccessibleInterval<T> in, final double isolevel, final VertexInterpolator interpolatorClass) {
		final Mesh result =
			(Mesh) ops().run(net.imagej.ops.geom.DefaultMarchingCubes.class, in, isolevel, interpolatorClass);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultMedianElongationFeature.class)
	public <B extends BooleanType<B>> DoubleType medianelongation(final IterableRegion<B> in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultMedianElongationFeature.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultMinorAxis.class)
	public DoubleType minoraxis(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultMinorAxis.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultRoundness.class)
	public DoubleType roundness(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultRoundness.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultRugosityFeature.class)
	public DoubleType rugosity(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultRugosityFeature.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultMinorMajorAxis.class)
	public Pair<DoubleType, DoubleType> secondmultivariate(final Polytope in) {
		final Pair<DoubleType, DoubleType> result =
			(Pair<DoubleType, DoubleType>) ops().run(net.imagej.ops.geom.DefaultMinorMajorAxis.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultSecondMultiVariate3D.class)
	public <B extends BooleanType<B>> CovarianceOf2ndMultiVariate3D secondmultivariate(final IterableRegion<B> in) {
		final CovarianceOf2ndMultiVariate3D result =
			(CovarianceOf2ndMultiVariate3D) ops().run(net.imagej.ops.geom.DefaultSecondMultiVariate3D.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultSizeFromPolygon.class)
	public DoubleType size(final ThePolygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultSizeFromPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultSizeFromIterableRegion.class)
	public <B extends BooleanType<B>> DoubleType size(final IterableRegion<B> in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultSizeFromIterableRegion.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultSizeFromMesh.class)
	public DoubleType size(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultSizeFromMesh.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultConvexHullSize.class)
	public DoubleType sizeconvexhull(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultConvexHullSize.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultSmallestEnclosingRectangle.class)
	public ThePolygon smallestboundingbox(final Polytope in) {
		final ThePolygon result =
			(ThePolygon) ops().run(net.imagej.ops.geom.DefaultSmallestEnclosingRectangle.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultSolidityFeature.class)
	public DoubleType solidity(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultSolidityFeature.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultSparenessFeature.class)
	public <B extends BooleanType<B>> DoubleType spareness(final IterableRegion<B> in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultSparenessFeature.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultSphericityFeature.class)
	public DoubleType sphericity(final Polytope in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.DefaultSphericityFeature.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultVertexInterpolator.class)
	public double[] vertexinterpolator(final int[] p1, final int[] p2, final double p1Value, final double p2Value, final double isolevel) {
		final double[] result =
			(double[]) ops().run(net.imagej.ops.geom.DefaultVertexInterpolator.class, p1, p2, p1Value, p2Value, isolevel);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.BitTypeVertexInterpolator.class)
	public double[] vertexinterpolator(final int[] p1, final int[] p2, final double p1Value, final double p2Value) {
		final double[] result =
			(double[]) ops().run(net.imagej.ops.geom.BitTypeVertexInterpolator.class, p1, p2, p1Value, p2Value);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultBoundingBox.class)
	public ThePolygon boundingbox(final ThePolygon in) {
		final ThePolygon result =
			(ThePolygon) ops().run(net.imagej.ops.geom.DefaultBoundingBox.class, in);
		return result;
	}
}
