/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops.Geometric.Voxelization;
import net.imagej.ops.geom.geom2d.DefaultConvexHull2D;
import net.imagej.ops.geom.geom3d.CovarianceOf2ndMultiVariate3D;
import net.imagej.ops.geom.geom3d.DefaultConvexHull3D;
import net.imagej.ops.geom.geom3d.mesh.Mesh;
import net.imagej.ops.geom.geom3d.mesh.VertexInterpolator;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

/**
 * Namespace for Geom.
 * 
 * @author Tim-Oliver Buchholz (University of Konstanz)
 */
@Plugin(type = Namespace.class)
public class GeomNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "geom";
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSurfacePixelCount.class)
	public DoubleType boundaryPixelCount(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.BoundaryPixelCount.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSurfacePixelCount.class)
	public DoubleType boundaryPixelCount(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultSurfacePixelCount.class, out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom2d.BoundaryPixelCountConvexHullPolygon.class)
	public DoubleType boundaryPixelCountConvexHull(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.BoundaryPixelCountConvexHull.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom3d.BoundaryPixelCountConvexHullMesh.class)
	public DoubleType boundaryPixelCountConvexHull(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.BoundaryPixelCountConvexHull.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.BoundaryPixelCountConvexHullPolygon.class)
	public DoubleType boundaryPixelCountConvexHull(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.BoundaryPixelCountConvexHullPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.BoundaryPixelCountConvexHullMesh.class)
	public DoubleType boundaryPixelCountConvexHull(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.BoundaryPixelCountConvexHullMesh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSurfaceArea.class)
	public DoubleType boundarySize(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.BoundarySize.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSurfaceArea.class)
	public DoubleType boundarySize(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultSurfaceArea.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultPerimeterLength.class)
	public DoubleType boundarySize(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultPerimeterLength.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultPerimeterLength.class)
	public DoubleType boundarySize(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultPerimeterLength.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.BoundarySizeConvexHullPolygon.class)
	public DoubleType boundarySizeConvexHull(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.BoundarySizeConvexHull.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.BoundarySizeConvexHullMesh.class)
	public DoubleType boundarySizeConvexHull(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.BoundarySizeConvexHull.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.BoundarySizeConvexHullPolygon.class)
	public DoubleType boundarySizeConvexHull(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.BoundarySizeConvexHullPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.BoundarySizeConvexHullMesh.class)
	public DoubleType boundarySizeConvexHull(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.BoundarySizeConvexHullMesh.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.BoxivityPolygon.class)
	public DoubleType boxivity(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.BoxivityPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.BoxivityMesh.class)
	public DoubleType boxivity(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.BoxivityMesh.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.BoxivityPolygon.class)
	public DoubleType boxivity(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.BoxivityPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.BoxivityMesh.class)
	public DoubleType boxivity(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.BoxivityMesh.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.DefaultCenterOfGravity.class)
	public <T extends RealType<T>> RealLocalizable centerOfGravity(final IterableInterval<T> in) {
		final RealLocalizable result = (RealLocalizable) ops().run(net.imagej.ops.Ops.Geometric.CenterOfGravity.class,
				in);
		return result;
	}	

	@OpMethod(op = net.imagej.ops.geom.CentroidLabelRegion.class)
	public RealLocalizable centroid(final LabelRegion<?> in) {
		final RealLocalizable result = (RealLocalizable) ops().run(
			net.imagej.ops.Ops.Geometric.Centroid.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.CentroidII.class)
	public RealLocalizable centroid(final IterableInterval<?> in) {
		final RealLocalizable result = (RealLocalizable) ops().run(
			net.imagej.ops.Ops.Geometric.Centroid.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.CentroidPolygon.class)
	public RealLocalizable centroid(final Polygon in) {
		final RealLocalizable result = (RealLocalizable) ops().run(
			net.imagej.ops.Ops.Geometric.Centroid.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.CentroidMesh.class)
	public RealLocalizable centroid(final Mesh in) {
		final RealLocalizable result = (RealLocalizable) ops().run(
			net.imagej.ops.Ops.Geometric.Centroid.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultCircularity.class)
	public DoubleType circularity(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultCircularity.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultCircularity.class)
	public DoubleType circularity(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultCircularity.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultCompactness.class)
	public DoubleType compactness(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.Compactness.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultCompactness.class)
	public DoubleType compactness(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultCompactness.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultContour.class)
	public <T extends Type<T>> Polygon contour(
		final RandomAccessibleInterval<T> in, final boolean useJacobs)
	{
		final Polygon result = (Polygon) ops().run(
			net.imagej.ops.Ops.Geometric.Contour.class, in, useJacobs);
		return result;
	}

	@OpMethod(op = DefaultConvexHull2D.class)
	public Polygon convexHull(final Polygon in) {
		final Polygon result = (Polygon) ops().run(
			net.imagej.ops.Ops.Geometric.ConvexHull.class, in);
		return result;
	}

	@OpMethod(op = DefaultConvexHull3D.class)
	public Mesh convexHull(final Mesh in) {
		final Mesh result = (Mesh) ops().run(
			net.imagej.ops.Ops.Geometric.ConvexHull.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVoxelization3D.class)
	public RandomAccessibleInterval<BitType> voxelization(final Mesh in, final int width, final int height, final int depth ) {
		final RandomAccessibleInterval<BitType> result = (RandomAccessibleInterval<BitType>) ops().run(
				Voxelization.class, in, width, height, depth );
	 	return result;
	}	

	@OpMethod(op = net.imagej.ops.geom.geom2d.ConvexityPolygon.class)
	public DoubleType convexity(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.ConvexityPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.ConvexityMesh.class)
	public DoubleType convexity(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.ConvexityMesh.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.ConvexityPolygon.class)
	public DoubleType convexity(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.ConvexityPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.ConvexityMesh.class)
	public DoubleType convexity(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.ConvexityMesh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultEccentricity.class)
	public DoubleType eccentricity(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultEccentricity.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultEccentricity.class)
	public DoubleType eccentricity(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultEccentricity.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeret.class)
	public Pair<RealLocalizable, RealLocalizable> feret(final Polygon in) {
		@SuppressWarnings("unchecked")
		final Pair<RealLocalizable, RealLocalizable> result =
			(Pair<RealLocalizable, RealLocalizable>) ops().run(
				net.imagej.ops.Ops.Geometric.Feret.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeretsAngle.class)
	public DoubleType feretsAngle(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultFeretsAngle.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeretsAngle.class)
	public DoubleType feretsAngle(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultFeretsAngle.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeretsDiameter.class)
	public DoubleType feretsDiameter(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultFeretsDiameter.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeretsDiameter.class)
	public DoubleType feretsDiameter(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultFeretsDiameter.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMainElongation.class)
	public <B extends BooleanType<B>> DoubleType mainElongation(
		final IterableRegion<B> in)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.MainElongation.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMainElongation.class)
	public DoubleType mainElongation(final DoubleType out, final IterableRegion in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultMainElongation.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultElongation.class)
	public DoubleType mainElongation(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultElongation.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultElongation.class)
	public DoubleType mainElongation(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultElongation.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMajorAxis.class)
	public DoubleType majorAxis(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMajorAxis.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMajorAxis.class)
	public DoubleType majorAxis(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMajorAxis.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMarchingCubes.class)
	public <T extends Type<T>> Mesh marchingCubes(
		final RandomAccessibleInterval<T> in)
	{
		final Mesh result = (Mesh) ops().run(
			net.imagej.ops.Ops.Geometric.MarchingCubes.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMarchingCubes.class)
	public <T extends Type<T>> Mesh marchingCubes(
		final RandomAccessibleInterval<T> in, final double isolevel)
	{
		final Mesh result = (Mesh) ops().run(
			net.imagej.ops.Ops.Geometric.MarchingCubes.class, in, isolevel);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMarchingCubes.class)
	public <T extends Type<T>> Mesh marchingCubes(
		final RandomAccessibleInterval<T> in, final double isolevel,
		final VertexInterpolator interpolatorClass)
	{
		final Mesh result = (Mesh) ops().run(
			net.imagej.ops.Ops.Geometric.MarchingCubes.class, in, isolevel,
			interpolatorClass);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMedianElongation.class)
	public <B extends BooleanType<B>> DoubleType medianElongation(
		final IterableRegion<B> in)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.MedianElongation.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMedianElongation.class)
	public DoubleType medianElongation(final DoubleType out, final IterableRegion in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultMedianElongation.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinorAxis.class)
	public DoubleType minorAxis(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMinorAxis.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinorAxis.class)
	public DoubleType minorAxis(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMinorAxis.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultRoundness.class)
	public DoubleType roundness(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultRoundness.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultRoundness.class)
	public DoubleType roundness(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultRoundness.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.RugosityPolygon.class)
	public DoubleType rugosity(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.RugosityPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.RugosityMesh.class)
	public DoubleType rugosity(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.RugosityMesh.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.RugosityPolygon.class)
	public DoubleType rugosity(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.RugosityPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.RugosityMesh.class)
	public DoubleType rugosity(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.RugosityMesh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinorMajorAxis.class)
	public Pair<DoubleType, DoubleType> secondMultiVariate(final Polygon in) {
		@SuppressWarnings("unchecked")
		final Pair<DoubleType, DoubleType> result =
			(Pair<DoubleType, DoubleType>) ops().run(
				net.imagej.ops.Ops.Geometric.SecondMultiVariate.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSecondMultiVariate3D.class)
	public <B extends BooleanType<B>> CovarianceOf2ndMultiVariate3D
		secondMultiVariate(final IterableRegion<B> in)
	{
		final CovarianceOf2ndMultiVariate3D result =
			(CovarianceOf2ndMultiVariate3D) ops().run(
				net.imagej.ops.Ops.Geometric.SecondMultiVariate.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultSizePolygon.class)
	public DoubleType size(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultSizePolygon.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultSizePolygon.class)
	public DoubleType size(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultSizePolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.SizeII.class)
	public DoubleType size(final IterableInterval<?> in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.Size.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.mesh.DefaultVolume.class)
	public DoubleType size(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.Size.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.SizeConvexHullPolygon.class)
	public DoubleType sizeConvexHull(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.Ops.Geometric.SizeConvexHull.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.SizeConvexHullMesh.class)
	public DoubleType sizeConvexHull(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.SizeConvexHull.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.SizeConvexHullPolygon.class)
	public DoubleType sizeConvexHull(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.SizeConvexHullPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.SizeConvexHullMesh.class)
	public DoubleType sizeConvexHull(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.SizeConvexHullMesh.class, out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom2d.DefaultSmallestEnclosingRectangle.class)
	public Polygon smallestEnclosingBoundingBox(final Polygon in) {
		final Polygon result = (Polygon) ops().run(
			net.imagej.ops.Ops.Geometric.SmallestEnclosingBoundingBox.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.SolidityPolygon.class)
	public DoubleType solidity(final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.SolidityPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.SolidityMesh.class)
	public DoubleType solidity(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.SolidityMesh.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.SolidityPolygon.class)
	public DoubleType solidity(final DoubleType out, final Polygon in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.SolidityPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.SolidityMesh.class)
	public DoubleType solidity(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.SolidityMesh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSpareness.class)
	public <B extends BooleanType<B>> DoubleType spareness(
		final IterableRegion<B> in)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.Spareness.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSpareness.class)
	public <B extends BooleanType<B>> DoubleType spareness(final DoubleType out, final IterableRegion<B> in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultSpareness.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSphericity.class)
	public DoubleType sphericity(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.Sphericity.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSphericity.class)
	public DoubleType sphericity(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultSphericity.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultBoundingBox.class)
	public Polygon boundingBox(final Polygon in) {
		final Polygon result = (Polygon) ops().run(
			net.imagej.ops.Ops.Geometric.BoundingBox.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom3d.mesh.DefaultVertexInterpolator.class)
	public double[] vertexInterpolator(final int[] p1, final int[] p2,
		final double p1Value, final double p2Value, final double isolevel)
	{
		final double[] result = (double[]) ops().run(
			net.imagej.ops.Ops.Geometric.VertexInterpolator.class, p1, p2,
			p1Value, p2Value, isolevel);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom3d.mesh.BitTypeVertexInterpolator.class)
	public double[] vertexInterpolator(final int[] p1, final int[] p2,
		final double p1Value, final double p2Value)
	{
		final double[] result = (double[]) ops().run(
			net.imagej.ops.Ops.Geometric.VertexInterpolator.class, p1, p2,
			p1Value, p2Value);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVoxelization3D.class)
	public RandomAccessibleInterval<BitType> voxelization(final Mesh in) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<BitType> result =
			(RandomAccessibleInterval<BitType>) ops().run(net.imagej.ops.geom.geom3d.DefaultVoxelization3D.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVoxelization3D.class)
	public RandomAccessibleInterval<BitType> voxelization(final Mesh in, final int width) {
		@SuppressWarnings("unchecked")
		final RandomAccessibleInterval<BitType> result =
			(RandomAccessibleInterval<BitType>) ops().run(net.imagej.ops.geom.geom3d.DefaultVoxelization3D.class, in, width);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVoxelization3D.class)
	public RandomAccessibleInterval<BitType> voxelization(final Mesh in, final int width, final int height) {
		final RandomAccessibleInterval<BitType> result =
			(RandomAccessibleInterval<BitType>) ops().run(net.imagej.ops.geom.geom3d.DefaultVoxelization3D.class, in, width, height);
		return result;
	}
}
