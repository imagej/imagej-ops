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

package net.imagej.ops.geom;

import java.util.List;

import net.imagej.mesh.Mesh;
import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.Ops.Geometric.Voxelization;
import net.imagej.ops.geom.geom3d.mesh.VertexInterpolator;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.geom.real.Polygon2D;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.apache.commons.math3.linear.RealMatrix;
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

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVerticesCountMesh.class)
	public DoubleType boundaryPixelCount(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.VerticesCount.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVerticesCountMesh.class)
	public DoubleType boundaryPixelCount(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultVerticesCountMesh.class, out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom2d.DefaultVerticesCountConvexHullPolygon.class)
	public DoubleType boundaryPixelCountConvexHull(final Polygon2D in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.VerticesCountConvexHull.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom3d.DefaultVerticesCountConvexHullMesh.class)
	public DoubleType boundaryPixelCountConvexHull(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.VerticesCountConvexHull.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultVerticesCountConvexHullPolygon.class)
	public DoubleType boundaryPixelCountConvexHull(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultVerticesCountConvexHullPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVerticesCountConvexHullMesh.class)
	public DoubleType boundaryPixelCountConvexHull(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultVerticesCountConvexHullMesh.class, out, in);
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
	public DoubleType boundarySize(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultPerimeterLength.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultPerimeterLength.class)
	public DoubleType boundarySize(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultPerimeterLength.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultBoundarySizeConvexHullPolygon.class)
	public DoubleType boundarySizeConvexHull(final Polygon2D in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.BoundarySizeConvexHull.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSurfaceAreaConvexHullMesh.class)
	public DoubleType boundarySizeConvexHull(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.BoundarySizeConvexHull.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultBoundarySizeConvexHullPolygon.class)
	public DoubleType boundarySizeConvexHull(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultBoundarySizeConvexHullPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSurfaceAreaConvexHullMesh.class)
	public DoubleType boundarySizeConvexHull(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultSurfaceAreaConvexHullMesh.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultBoxivityPolygon.class)
	public DoubleType boxivity(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultBoxivityPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultBoxivityMesh.class)
	public DoubleType boxivity(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultBoxivityMesh.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultBoxivityPolygon.class)
	public DoubleType boxivity(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultBoxivityPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultBoxivityMesh.class)
	public DoubleType boxivity(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultBoxivityMesh.class, out, in);
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
	public RealLocalizable centroid(final Polygon2D in) {
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
	public DoubleType circularity(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultCircularity.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultCircularity.class)
	public DoubleType circularity(final DoubleType out, final Polygon2D in) {
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
	public <T extends Type<T>> Polygon2D contour(
		final RandomAccessibleInterval<T> in, final boolean useJacobs)
	{
		final Polygon2D result = (Polygon2D) ops().run(
			net.imagej.ops.Ops.Geometric.Contour.class, in, useJacobs);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultConvexHull2D.class)
	public Polygon2D convexHull(final Polygon2D in) {
		final Polygon2D result = (Polygon2D) ops().run(
			net.imagej.ops.Ops.Geometric.ConvexHull.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultConvexHull3D.class)
	public List convexHull(final Mesh in) {
		final List<?> result = (List<?>) ops().run(
			net.imagej.ops.Ops.Geometric.ConvexHull.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVoxelization3D.class)
	public RandomAccessibleInterval<BitType> voxelization(final Mesh in, final int width, final int height, final int depth ) {
		final RandomAccessibleInterval<BitType> result = (RandomAccessibleInterval<BitType>) ops().run(
				Voxelization.class, in, width, height, depth );
	 	return result;
	}	

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultConvexityPolygon.class)
	public DoubleType convexity(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultConvexityPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultConvexityMesh.class)
	public DoubleType convexity(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultConvexityMesh.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultConvexityPolygon.class)
	public DoubleType convexity(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultConvexityPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultConvexityMesh.class)
	public DoubleType convexity(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultConvexityMesh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultEccentricity.class)
	public DoubleType eccentricity(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultEccentricity.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultEccentricity.class)
	public DoubleType eccentricity(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultEccentricity.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeretsDiameterForAngle.class)
	public DoubleType feretsDiameter(final Polygon2D in, final double angle) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultFeretsDiameterForAngle.class, in, angle);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeretsDiameterForAngle.class)
	public DoubleType feretsDiameter(final DoubleType out, final Polygon2D in, final double angle) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultFeretsDiameterForAngle.class, out, in, angle);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeretsAngle.class)
	public DoubleType feretsAngle(final Pair<RealLocalizable, RealLocalizable> in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultFeretsAngle.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeretsAngle.class)
	public DoubleType feretsAngle(final DoubleType out, final Pair<RealLocalizable, RealLocalizable> in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultFeretsAngle.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeretsDiameter.class)
	public DoubleType feretsDiameter(final Pair<RealLocalizable, RealLocalizable> in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultFeretsDiameter.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultFeretsDiameter.class)
	public DoubleType feretsDiameter(final DoubleType out, final Pair<RealLocalizable, RealLocalizable> in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultFeretsDiameter.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMaximumFeret.class)
	public Pair<RealLocalizable, RealLocalizable> maximumFeret(final Polygon2D in) {
		@SuppressWarnings("unchecked")
		final Pair<RealLocalizable, RealLocalizable> result =
			(Pair<RealLocalizable, RealLocalizable>) ops().run(net.imagej.ops.geom.geom2d.DefaultMaximumFeret.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMaximumFeretDiameter.class)
	public DoubleType maximumFeretsDiameter(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMaximumFeretDiameter.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMaximumFeretDiameter.class)
	public DoubleType maximumFeretsDiameter(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMaximumFeretDiameter.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMaximumFeretAngle.class)
	public DoubleType maximumFeretsAngle(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMaximumFeretAngle.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMaximumFeretAngle.class)
	public DoubleType maximumFeretsAngle(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMaximumFeretAngle.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinimumFeret.class)
	public Pair<RealLocalizable, RealLocalizable> minimumFeret(final Polygon2D in) {
		@SuppressWarnings("unchecked")
		final Pair<RealLocalizable, RealLocalizable> result =
			(Pair<RealLocalizable, RealLocalizable>) ops().run(net.imagej.ops.geom.geom2d.DefaultMinimumFeret.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinimumFeretAngle.class)
	public DoubleType minimumFeretsAngle(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMinimumFeretAngle.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinimumFeretAngle.class)
	public DoubleType minimumFeretsAngle(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMinimumFeretAngle.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinimumFeretDiameter.class)
	public DoubleType minimumFeretsDiameter(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMinimumFeretDiameter.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinimumFeretDiameter.class)
	public DoubleType minimumFeretsDiameter(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMinimumFeretDiameter.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultElongation.class)
	public DoubleType mainElongation(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultElongation.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultElongation.class)
	public DoubleType mainElongation(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultElongation.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMajorAxis.class)
	public DoubleType majorAxis(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMajorAxis.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMajorAxis.class)
	public DoubleType majorAxis(final DoubleType out, final Polygon2D in) {
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
	public DoubleType medianElongation(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultMedianElongation.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMedianElongation.class)
	public DoubleType medianElongation(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultMedianElongation.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinorAxis.class)
	public DoubleType minorAxis(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMinorAxis.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinorAxis.class)
	public DoubleType minorAxis(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultMinorAxis.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultRoundness.class)
	public DoubleType roundness(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultRoundness.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultRoundness.class)
	public DoubleType roundness(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultRoundness.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultSizePolygon.class)
	public DoubleType size(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultSizePolygon.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultSizePolygon.class)
	public DoubleType size(final DoubleType out, final Polygon2D in) {
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

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVolumeMesh.class)
	public DoubleType size(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.Size.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultSizeConvexHullPolygon.class)
	public DoubleType sizeConvexHull(final Polygon2D in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.Ops.Geometric.SizeConvexHull.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVolumeConvexHullMesh.class)
	public DoubleType sizeConvexHull(final Mesh in) {
		final DoubleType result = (DoubleType) ops().run(
			net.imagej.ops.Ops.Geometric.SizeConvexHull.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultSizeConvexHullPolygon.class)
	public DoubleType sizeConvexHull(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultSizeConvexHullPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVolumeConvexHullMesh.class)
	public DoubleType sizeConvexHull(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultVolumeConvexHullMesh.class, out, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.geom2d.DefaultSmallestEnclosingRectangle.class)
	public Polygon2D smallestEnclosingBoundingBox(final Polygon2D in) {
		final Polygon2D result = (Polygon2D) ops().run(
			net.imagej.ops.Ops.Geometric.SmallestEnclosingBoundingBox.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultSolidityPolygon.class)
	public DoubleType solidity(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultSolidityPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSolidityMesh.class)
	public DoubleType solidity(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultSolidityMesh.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultSolidityPolygon.class)
	public DoubleType solidity(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultSolidityPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSolidityMesh.class)
	public DoubleType solidity(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultSolidityMesh.class, out, in);
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
	public Polygon2D boundingBox(final Polygon2D in) {
		final Polygon2D result = (Polygon2D) ops().run(
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
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultVerticesCountPolygon.class)
	public DoubleType verticesCount(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultVerticesCountPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultVerticesCountPolygon.class)
	public DoubleType verticesCount(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultVerticesCountPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVerticesCountMesh.class)
	public DoubleType verticesCount(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultVerticesCountMesh.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVerticesCountMesh.class)
	public DoubleType verticesCount(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultVerticesCountMesh.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultVerticesCountConvexHullPolygon.class)
	public DoubleType verticesCountConvexHull(final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultVerticesCountConvexHullPolygon.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultVerticesCountConvexHullPolygon.class)
	public DoubleType verticesCountConvexHull(final DoubleType out, final Polygon2D in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom2d.DefaultVerticesCountConvexHullPolygon.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVerticesCountConvexHullMesh.class)
	public DoubleType verticesCountConvexHull(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultVerticesCountConvexHullMesh.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultVerticesCountConvexHullMesh.class)
	public DoubleType verticesCountConvexHull(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultVerticesCountConvexHullMesh.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMainElongation.class)
	public DoubleType mainElongation(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultMainElongation.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultMainElongation.class)
	public DoubleType mainElongation(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultMainElongation.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom2d.DefaultMinorMajorAxis.class)
	public Pair<DoubleType, DoubleType> secondMoment(final Polygon2D in) {
		@SuppressWarnings("unchecked")
		final Pair<DoubleType, DoubleType> result =
			(Pair<DoubleType, DoubleType>) ops().run(net.imagej.ops.geom.geom2d.DefaultMinorMajorAxis.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultInertiaTensor3DMesh.class)
	public RealMatrix secondMoment(final Mesh in) {
		final RealMatrix result =
			(RealMatrix) ops().run(net.imagej.ops.geom.geom3d.DefaultInertiaTensor3DMesh.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.mesh.DefaultSmallestOrientedBoundingBox.class)
	public Mesh smallestEnclosingBoundingBox(final Mesh in) {
		final Mesh result =
			(Mesh) ops().run(net.imagej.ops.geom.geom3d.mesh.DefaultSmallestOrientedBoundingBox.class, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSparenessMesh.class)
	public DoubleType spareness(final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultSparenessMesh.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultSparenessMesh.class)
	public DoubleType spareness(final DoubleType out, final Mesh in) {
		final DoubleType result =
			(DoubleType) ops().run(net.imagej.ops.geom.geom3d.DefaultSparenessMesh.class, out, in);
		return result;
	}
	
	@OpMethod(op = net.imagej.ops.geom.geom3d.DefaultInertiaTensor3D.class)
	public RealMatrix secondMoment(final IterableRegion in) {
		final RealMatrix result =
			(RealMatrix) ops().run(net.imagej.ops.geom.geom3d.DefaultInertiaTensor3D.class, in);
		return result;
	}
}
