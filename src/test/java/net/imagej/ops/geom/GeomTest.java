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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import net.imagej.ops.Ops;
import net.imagej.ops.features.AbstractFeatureTest;
import net.imagej.ops.geom.geom2d.DefaultCircularity;
import net.imagej.ops.geom.geom2d.DefaultContour;
import net.imagej.ops.geom.geom2d.DefaultEccentricity;
import net.imagej.ops.geom.geom2d.DefaultFeretsAngle;
import net.imagej.ops.geom.geom2d.DefaultFeretsDiameter;
import net.imagej.ops.geom.geom2d.DefaultMajorAxis;
import net.imagej.ops.geom.geom2d.DefaultMinorAxis;
import net.imagej.ops.geom.geom2d.DefaultPerimeterLength;
import net.imagej.ops.geom.geom2d.DefaultRoundness;
import net.imagej.ops.geom.geom2d.DefaultSizePolygon;
import net.imagej.ops.geom.geom2d.SolidityPolygon;
import net.imagej.ops.geom.geom3d.BoundaryPixelCountConvexHullMesh;
import net.imagej.ops.geom.geom3d.BoundarySizeConvexHullMesh;
import net.imagej.ops.geom.geom3d.ConvexityMesh;
import net.imagej.ops.geom.geom3d.DefaultCompactness;
import net.imagej.ops.geom.geom3d.DefaultMainElongation;
import net.imagej.ops.geom.geom3d.DefaultMarchingCubes;
import net.imagej.ops.geom.geom3d.DefaultMedianElongation;
import net.imagej.ops.geom.geom3d.DefaultSpareness;
import net.imagej.ops.geom.geom3d.DefaultSphericity;
import net.imagej.ops.geom.geom3d.DefaultSurfaceArea;
import net.imagej.ops.geom.geom3d.DefaultSurfacePixelCount;
import net.imagej.ops.geom.geom3d.RugosityMesh;
import net.imagej.ops.geom.geom3d.SizeConvexHullMesh;
import net.imagej.ops.geom.geom3d.SolidityMesh;
import net.imagej.ops.geom.geom3d.mesh.Mesh;
import net.imglib2.IterableInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.img.Img;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;

import net.imglib2.view.Views;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for geom features
 * 
 * @author Daniel Seebacher (University of Konstanz)
 * @author Tim-Oliver Buchholz (University of Konstanz)
 * @author Robert Haase (MPI CBG Dresden)
 */
public class GeomTest extends AbstractFeatureTest {

	private static LabelRegion<String> region2D;
	private static LabelRegion<String> region3D;
	private static Img<FloatType> img2d;
	private Polygon contour;
	private Mesh mesh;

	@BeforeClass
	public static void setupBefore() {
		img2d = getTestImage2D();
		region2D = createLabelRegion(img2d, 0, 0);

		// HACK: Invert the labeling depending on which TIFF we are using.
		// This is a workaround to the fact that the "expensive" 3D TIFF
		// _used to_ be imported with inverted pixel values (due to a
		// PhotometricInterpretation of 0 instead of 1), such that the
		// central object was all 0s rather than all 255s. And I think
		// the expected values in the tests here may be wrong as a result.
		// FIXME: Double check what is going on here!
		float min = expensiveTestsEnabled ? 0 : 1;
		float max = expensiveTestsEnabled ? 0 : 255;
		region3D = createLabelRegion(getTestImage3D(), min, max, 104, 102, 81);
	}

	@Override
	public void setup() {
		// no implementation is needed since the tests in this class will not use
		// the features provided in super#setup()
		contour = (Polygon) ops.run(DefaultContour.class, region2D, true);
		mesh = (Mesh) ops.run(DefaultMarchingCubes.class, region3D);
	}

	@Test
	public void createPolygon() {
		ops.run(DefaultContour.class, region2D, true);
	}

	@Test
	public void testSize() {
		final double expected = expensiveTestsEnabled ? 355588.5 : 3456.0;
		assertEquals(Ops.Geometric.Size.NAME, expected, ((DoubleType) ops.run(
			DefaultSizePolygon.class, contour)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testBoundarySize2D() {
		final double expected = expensiveTestsEnabled ? 2658.990257670
			: 261.563491861041;
		assertEquals(Ops.Geometric.BoundarySize.NAME, expected, ((DoubleType) ops
			.run(DefaultPerimeterLength.class, contour)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testCircularity() {
		final double expected = expensiveTestsEnabled ? 0.632083948
			: 0.6347889302936764;
		assertEquals(Ops.Geometric.Circularity.NAME, expected, ((DoubleType) ops
			.run(DefaultCircularity.class, contour)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testMinorAxis() {
		final double expected = expensiveTestsEnabled ? 520.6209074990064
			: 51.0180931669664;
		assertEquals(Ops.Geometric.MinorAxis.NAME, expected, ((DoubleType) ops.run(
			DefaultMinorAxis.class, contour)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testMajorAxis() {
		final double expected = expensiveTestsEnabled ? 869.6334191187347
			: 86.25010449143703;
		assertEquals(Ops.Geometric.MajorAxis.NAME, expected, ((DoubleType) ops.run(
			DefaultMajorAxis.class, contour)).getRealDouble(), 0.01);
	}

	@Test
	public void testFeretsDiameter() {
		final double expected = expensiveTestsEnabled ? 908.002202641
			: 89.888820216;
		assertEquals(Ops.Geometric.FeretsDiameter.NAME, expected, ((DoubleType) ops
			.run(DefaultFeretsDiameter.class, contour)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testFeretsAngle() {
		// angle could be reversed so check
		// 148.235410152 and
		// 148.235410152.. + 180
		final double expectedAngle = expensiveTestsEnabled ? 148.235410152
			: 147.724355685;
		final double actualAngle = ((DoubleType) ops.run(DefaultFeretsAngle.class,
			contour)).getRealDouble();

		boolean isEquals = false;
		if (Math.abs(expectedAngle -
			actualAngle) < AbstractFeatureTest.SMALL_DELTA || Math.abs(expectedAngle +
				180 - actualAngle) < AbstractFeatureTest.SMALL_DELTA)
		{
			isEquals = true;
		}

		assertTrue(Ops.Geometric.FeretsAngle.NAME + " Expected [" + expectedAngle +
			"] was [" + actualAngle + "]", isEquals);
	}

	@Test
	public void testEccentricity() {
		final double expected = expensiveTestsEnabled ? 1.670273923 : 1.689817582;
		assertEquals(Ops.Geometric.Eccentricity.NAME, expected, ((DoubleType) ops
			.run(DefaultEccentricity.class, contour)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testRoundness() {
		final double expected = expensiveTestsEnabled ? 0.598704192 : 0.591779852;
		assertEquals(Ops.Geometric.Roundness.NAME, expected, ((DoubleType) ops.run(
			DefaultRoundness.class, contour)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testCentroidMesh() {
		final double expected1 = expensiveTestsEnabled ? 25.700 : 12.237808234;
		final double expected2 = expensiveTestsEnabled ? 24.644 : 12.524262243;
		final double expected3 = expensiveTestsEnabled ? 19.945 : 9.568196449;
		final RealPoint c = (RealPoint) ops.run(CentroidMesh.class, mesh);
		assertEquals(expected1, c.getDoublePosition(0),
			AbstractFeatureTest.BIG_DELTA);
		assertEquals(expected2, c.getDoublePosition(1),
			AbstractFeatureTest.BIG_DELTA);
		assertEquals(expected3, c.getDoublePosition(2),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testCentroidMeshVersusRegion3D() {
		// the centroid may differ between a mesh and the binary image it's derived from by not more than cubic root of
		// half a pixel
		double tolerance = Math.cbrt(0.5);

		final RealPoint centroidMesh = (RealPoint) ops.geom().centroid(mesh);
		final RealPoint centroidRegion = (RealPoint) ops.run(Ops.Geometric.Centroid.class, region3D);

		for (int d = 0; d < centroidRegion.numDimensions(); d++) {
			assertEquals(centroidMesh.getDoublePosition(d), centroidRegion.getDoublePosition(d), tolerance);
		}
	}

	@Test
	public void testCentroidPolygonVersusRegion2D() {
		// the centroid may differ between a polygon and a rasterised polygon by not more than square root of half a
		// pixel
		double tolerance = Math.sqrt(0.5);

		final RealPoint centroidPolygon = (RealPoint) ops.run(CentroidPolygon.class, contour);
		final RealLocalizable centroidRasterisedPolygon = ops.geom().centroid(contour.rasterize());
		final RealPoint centroidRegion = (RealPoint) ops.run(Ops.Geometric.Centroid.class, region2D);

		for (int d = 0; d < centroidRegion.numDimensions(); d++) {
			assertEquals(centroidPolygon.getDoublePosition(d), centroidRasterisedPolygon.getDoublePosition(d), tolerance);
			assertEquals(centroidRasterisedPolygon.getDoublePosition(d), centroidRegion.getDoublePosition(d), tolerance);
		}
	}

	@Test
	public void testSolidity2D() {
		final double expected = expensiveTestsEnabled ? 0.997063173 : 0.976990400;
		assertEquals(Ops.Geometric.Solidity.NAME, expected, ((DoubleType) ops.run(
			SolidityPolygon.class, contour)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSolidity3D() {
		final double expected = expensiveTestsEnabled ? 0.754 : 0.905805001;
		// This test is just here for completeness.
		// All input values of solidity are verified.
		assertEquals(Ops.Geometric.Solidity.NAME, expected, ((DoubleType) ops.run(
			SolidityMesh.class, mesh)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testRugosity() {
		final double expected = expensiveTestsEnabled ? 1.379 : 1.035052196;
		// This test is just here for completeness.
		// All input values of rugosity are verified.
		assertEquals(Ops.Geometric.Rugosity.NAME, expected, ((DoubleType) ops.run(
			RugosityMesh.class, mesh)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testConvexity() {
		final double expected = expensiveTestsEnabled ? 0.725 : 0.966134851;
		// This test is just here for completeness.
		// All input values of convexity are verified.
		assertEquals(Ops.Geometric.Convexity.NAME, expected, ((DoubleType) ops.run(
			ConvexityMesh.class, mesh)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testBoundaryPixelCountConvexHull() {
		final double expected = expensiveTestsEnabled ? 32 : 179;
		// Verified by hand. qhull merges faces and therefore has another number
		// of surface pixels
		assertEquals(Ops.Geometric.BoundaryPixelCountConvexHull.NAME, expected,
			((DoubleType) ops.run(BoundaryPixelCountConvexHullMesh.class, mesh))
				.get(), AbstractFeatureTest.BIG_DELTA);

	}

	@Test
	public void testBoundarySizeConvexHull() {
		final double expected = expensiveTestsEnabled ? 13580.54 : 1457.276963378;
		// value taken from qhull (qhull.org)
		assertEquals(Ops.Geometric.BoundarySizeConvexHull.NAME, expected,
			((DoubleType) ops.run(BoundarySizeConvexHullMesh.class, mesh)).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSizeConvexHull() {
		final double expected = expensiveTestsEnabled ? 108660.667 : 4930.375000000;
		// value taken from qhull (qhull.org)
		assertEquals(Ops.Geometric.SizeConvexHull.NAME, expected, ((DoubleType) ops
			.run(SizeConvexHullMesh.class, mesh)).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testBoundarySize3D() {
		final double expected = expensiveTestsEnabled ? 18741.018 : 1508.357722350;
		// The delta is relatively big because they use float numbers in imagej
		// and my implementation is based on doubles.
		assertEquals(Ops.Geometric.BoundarySize.NAME, expected, ((DoubleType) ops
			.run(DefaultSurfaceArea.class, mesh)).get(), 0.186);
	}

	@Test
	public void testBoundaryPixelCount() {
		final double expected = expensiveTestsEnabled ? 20996.0 : 2070.0;
		assertEquals(Ops.Geometric.BoundaryPixelCount.NAME, expected,
			((DoubleType) ops.run(DefaultSurfacePixelCount.class, mesh)).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSize3D() {
		final double expected = expensiveTestsEnabled ? 81992 : 4488;
		assertEquals(Ops.Geometric.Size.NAME, expected, ((DoubleType) ops.run(
			SizeII.class, region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testCompactness() {
		final double expected = expensiveTestsEnabled ? 0.082 : 0.254314235;
		assertEquals(Ops.Geometric.Compactness.NAME, expected, ((DoubleType) ops
			.run(DefaultCompactness.class, mesh)).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSphericity() {
		final double expected = expensiveTestsEnabled ? 0.435 : 0.633563608;
		assertEquals(Ops.Geometric.Sphericity.NAME, expected, ((DoubleType) ops.run(
			DefaultSphericity.class, mesh)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testMainElongation() {
		final double expected = expensiveTestsEnabled ? 1.041 : 1.270370555;
		assertEquals(Ops.Geometric.MainElongation.NAME, expected, ((DoubleType) ops
			.run(DefaultMainElongation.class, region3D)).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testMedianElongation() {
		final double expected = expensiveTestsEnabled ? 1.225 : 1.137093214;
		assertEquals(Ops.Geometric.MedianElongation.NAME, expected,
			((DoubleType) ops.run(DefaultMedianElongation.class, region3D)).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSpareness() {
		final double expected = expensiveTestsEnabled ? 0.509 : 0.978261746;
		assertEquals(Ops.Geometric.Spareness.NAME, expected, ((DoubleType) ops.run(
			DefaultSpareness.class, region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testCenterOfGravity() {
		final double expected1 = expensiveTestsEnabled ? 396.06335171064376
			: 39.45544609181475;
		final double expected2 = expensiveTestsEnabled ? 576.764051738724
			: 57.58019063466828;
		final RealLocalizable result = (RealLocalizable) ops.run(
			DefaultCenterOfGravity.class, img2d);
		assertEquals(Ops.Geometric.CenterOfGravity.NAME, expected1, result
			.getDoublePosition(0), AbstractFeatureTest.BIG_DELTA);
		assertEquals(Ops.Geometric.CenterOfGravity.NAME, expected2, result
			.getDoublePosition(1), AbstractFeatureTest.BIG_DELTA);
	}

}
