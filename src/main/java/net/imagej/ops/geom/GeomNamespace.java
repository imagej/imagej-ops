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
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

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

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSurfacePixelCount.class)
	public DoubleType boundarypixelcount(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.DefaultSurfacePixelCount.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom2d.BoundaryPixelCountConvexHullPolygon.class)
	public DoubleType boundarypixelcountconvexhull(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.BoundaryPixelCountConvexHullPolygon.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom3d.BoundaryPixelCountConvexHullMesh.class)
	public DoubleType boundarypixelcountconvexhull(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.BoundaryPixelCountConvexHullMesh.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSurfaceArea.class)
	public DoubleType boundarysize(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.DefaultSurfaceArea.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultPerimeterLength.class)
	public DoubleType boundarysize(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.DefaultPerimeterLength.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.BoundarySizeConvexHullPolygon.class)
	public DoubleType boundarysizeconvexhull(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.BoundarySizeConvexHullPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.BoundarySizeConvexHullMesh.class)
	public DoubleType boundarysizeconvexhull(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.BoundarySizeConvexHullMesh.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.BoxivityPolygon.class)
	public DoubleType boxivity(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.BoxivityPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.BoxivityMesh.class)
	public DoubleType boxivity(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.BoxivityMesh.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.CentroidLabelRegion.class)
	public RealLocalizable centroid(final LabelRegion<?> in) {
		final RealLocalizable result = (RealLocalizable) ops().run(
			net.imagej.ops.geom.CentroidLabelRegion.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.CentroidII.class)
	public RealLocalizable centroid(final IterableInterval<?> in) {
		final RealLocalizable result = (RealLocalizable) ops().run(
			net.imagej.ops.geom.CentroidII.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.CentroidPolygon.class)
	public RealLocalizable centroid(final Polygon in) {
		final RealLocalizable result = (RealLocalizable) ops().run(
			net.imagej.ops.geom.CentroidPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.CentroidMesh.class)
	public RealLocalizable centroid(final Mesh in) {
		final RealLocalizable result = (RealLocalizable) ops().run(
			net.imagej.ops.geom.CentroidMesh.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultCircularity.class)
	public DoubleType circularity(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.DefaultCircularity.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultCompactness.class)
	public DoubleType compactness(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.DefaultCompactness.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultContour.class)
	public <T extends Type<T>> Polygon contour(
		final RandomAccessibleInterval<T> in, final boolean useJacobs,
		final boolean isInverted)
	{
		final Polygon result = (Polygon) ops().run(
			net.imagej.ops.geom.geom2d.DefaultContour.class, in, useJacobs,
			isInverted);
		return result;
	}

	@OpMethod(op = DefaultConvexHull2D.class)
	public Polygon convexhull(final Polygon in) {
		final Polygon result = (Polygon) ops().run(
			net.imagej.ops.Ops.Geometric.ConvexHull.class, in);
		return result;
	}

	@OpMethod(op = DefaultConvexHull3D.class)
	public Mesh convexhull(final Mesh in) {
		final Mesh result = (Mesh) ops().run(
			net.imagej.ops.Ops.Geometric.ConvexHull.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.ConvexityPolygon.class)
	public DoubleType convexity(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.ConvexityPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.ConvexityMesh.class)
	public DoubleType convexity(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.ConvexityMesh.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultEccentricity.class)
	public DoubleType eccentricity(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.DefaultEccentricity.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeret.class)
	public Pair<RealLocalizable, RealLocalizable> feret(final Polygon in) {
		@SuppressWarnings("unchecked")
		final Pair<RealLocalizable, RealLocalizable> result =
			(Pair<RealLocalizable, RealLocalizable>) ops().run(
				net.imagej.ops.geom.geom2d.DefaultFeret.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeretsAngle.class)
	public DoubleType feretsangle(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.DefaultFeretsAngle.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeretsDiameter.class)
	public DoubleType feretsdiameter(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.DefaultFeretsDiameter.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMainElongation.class)
	public <B extends BooleanType<B>> DoubleType mainelongation(
		final IterableRegion<B> in)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.DefaultMainElongation.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultElongation.class)
	public DoubleType mainelongation(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.DefaultElongation.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMajorAxis.class)
	public DoubleType majoraxis(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.DefaultMajorAxis.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMarchingCubes.class)
	public <T extends Type<T>> Mesh marchingcubes(
		final RandomAccessibleInterval<T> in)
	{
		final Mesh result = (Mesh) ops().run(
			net.imagej.ops.geom.geom3d.DefaultMarchingCubes.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMarchingCubes.class)
	public <T extends Type<T>> Mesh marchingcubes(
		final RandomAccessibleInterval<T> in, final double isolevel)
	{
		final Mesh result = (Mesh) ops().run(
			net.imagej.ops.geom.geom3d.DefaultMarchingCubes.class, in, isolevel);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMarchingCubes.class)
	public <T extends Type<T>> Mesh marchingcubes(
		final RandomAccessibleInterval<T> in, final double isolevel,
		final VertexInterpolator interpolatorClass)
	{
		final Mesh result = (Mesh) ops().run(
			net.imagej.ops.geom.geom3d.DefaultMarchingCubes.class, in, isolevel,
			interpolatorClass);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMedianElongation.class)
	public <B extends BooleanType<B>> DoubleType medianelongation(
		final IterableRegion<B> in)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.DefaultMedianElongation.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinorAxis.class)
	public DoubleType minoraxis(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.DefaultMinorAxis.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultRoundness.class)
	public DoubleType roundness(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.DefaultRoundness.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.RugosityPolygon.class)
	public DoubleType rugosity(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.RugosityPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.RugosityMesh.class)
	public DoubleType rugosity(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.RugosityMesh.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinorMajorAxis.class)
	public Pair<DoubleType, DoubleType> secondmultivariate(final Polygon in) {
		@SuppressWarnings("unchecked")
		final Pair<DoubleType, DoubleType> result =
			(Pair<DoubleType, DoubleType>) ops().run(
				net.imagej.ops.geom.geom2d.DefaultMinorMajorAxis.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSecondMultiVariate3D.class)
	public <B extends BooleanType<B>> CovarianceOf2ndMultiVariate3D
		secondmultivariate(final IterableRegion<B> in)
	{
		final CovarianceOf2ndMultiVariate3D result =
			(CovarianceOf2ndMultiVariate3D) ops().run(
				net.imagej.ops.geom.geom3d.DefaultSecondMultiVariate3D.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultSizePolygon.class)
	public DoubleType size(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.DefaultSizePolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.SizeII.class)
	public DoubleType size(final IterableInterval<?> in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.SizeII.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.mesh.DefaultVolume.class)
	public DoubleType size(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.mesh.DefaultVolume.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.SizeConvexHullMesh.class)
	public DoubleType sizeconvexhull(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.SizeConvexHullMesh.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.SizeConvexHullPolygon.class)
	public DoubleType sizeconvexhull(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.SizeConvexHullPolygon.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom2d.DefaultSmallestEnclosingRectangle.class)
	public Polygon smallestenclosingboundingbox(final Polygon in) {
		final Polygon result = (Polygon) ops().run(
			net.imagej.ops.geom.geom2d.DefaultSmallestEnclosingRectangle.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.SolidityPolygon.class)
	public DoubleType solidity(final Polygon in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom2d.SolidityPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.SolidityMesh.class)
	public DoubleType solidity(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.SolidityMesh.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSpareness.class)
	public <B extends BooleanType<B>> DoubleType spareness(
		final IterableRegion<B> in)
	{
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.DefaultSpareness.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSphericity.class)
	public DoubleType sphericity(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.geom.geom3d.DefaultSphericity.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultBoundingBox.class)
	public Polygon boundingbox(final Polygon in) {
		final Polygon result = (Polygon) ops().run(
			net.imagej.ops.geom.geom2d.DefaultBoundingBox.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom3d.mesh.DefaultVertexInterpolator.class)
	public double[] vertexinterpolator(final int[] p1, final int[] p2,
		final double p1Value, final double p2Value, final double isolevel)
	{
		final double[] result = (double[]) ops().run(
			net.imagej.ops.geom.geom3d.mesh.DefaultVertexInterpolator.class, p1, p2,
			p1Value, p2Value, isolevel);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom3d.mesh.BitTypeVertexInterpolator.class)
	public double[] vertexinterpolator(final int[] p1, final int[] p2,
		final double p1Value, final double p2Value)
	{
		final double[] result = (double[]) ops().run(
			net.imagej.ops.geom.geom3d.mesh.BitTypeVertexInterpolator.class, p1, p2,
			p1Value, p2Value);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultCenterOfGravity.class)
	public <T extends RealType<T>> RealLocalizable centerofgravity(final IterableInterval<T> in) {
		final RealLocalizable result = (RealLocalizable) ops().run(net.imagej.ops.geom.DefaultCenterOfGravity.class,
				in);
		return result;
	}
}
