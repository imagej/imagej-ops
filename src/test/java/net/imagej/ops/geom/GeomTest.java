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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;

import net.imagej.ops.Ops;
import net.imagej.ops.features.AbstractFeatureTest;
import net.imglib2.RealPoint;
import net.imglib2.img.Img;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for geom features
 * 
 * @author Daniel Seebacher, University of Konstanz
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
public class GeomTest extends AbstractFeatureTest {

	private static LabelRegion<String> region2D;
	private static LabelRegion<String> region3D;
	private static Img<FloatType> img2d;

	@BeforeClass
	public static void setupBefore() throws MalformedURLException, IOException {
		img2d = getTestImage2D();
		region2D = createLabelRegion2D();
		region3D = createLabelRegion3D();
	}

	@Override
	public void setup() {
		// no implementation is needed since the tests in this class will not use
		// the features provided in super#setup()
	}

	@Test
	public void createPolygon() {
		ops.geom().contour(region2D, true, true);
	}

	@Test
	public void testSize() {
		final double expected = expensiveTestsEnabled ? 355630.5 : 3460.5;
		assertEquals(Ops.Geometric.Size.NAME, expected, ops.geom().size(ops.geom()
			.contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testBoundarySize2D() {
		final double expected = expensiveTestsEnabled ? 2658.990257670
			: 262.977705423;
		assertEquals(Ops.Geometric.BoundarySize.NAME, expected, ops.geom()
			.boundarysize(ops.geom().contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testCircularity() {
		final double expected = expensiveTestsEnabled ? 0.632083948 : 0.628797569;
		assertEquals(Ops.Geometric.Circularity.NAME, expected, ops.geom()
			.circularity(ops.geom().contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testMinorAxis() {
		final double expected = expensiveTestsEnabled ? 520.667420750
			: 51.062793933;
		assertEquals(Ops.Geometric.MinorAxis.NAME, expected, ops.geom().minoraxis(
			ops.geom().contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testMajorAxis() {
		final double expected = expensiveTestsEnabled ? 869.657215429
			: 86.286806991;
		assertEquals(Ops.Geometric.MajorAxis.NAME, expected, ops.geom().majoraxis(
			ops.geom().contour(region2D, true, true)).getRealDouble(), 0.01);
	}

	@Test
	public void testFeretsDiameter() {
		final double expected = expensiveTestsEnabled ? 908.002202641
			: 89.888820216;
		assertEquals(Ops.Geometric.FeretsDiameter.NAME, expected, ops.geom()
			.feretsdiameter(ops.geom().contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testFeretsAngle() {
		// angle could be reversed so check
		// 148.235410152 and
		// 148.235410152.. + 180
		final double expectedAngle = expensiveTestsEnabled ? 148.235410152
			: 147.724355685;
		final double actualAngle = ops.geom().feretsangle(ops.geom().contour(
			region2D, true, true)).getRealDouble();

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
		assertEquals(Ops.Geometric.Eccentricity.NAME, expected, ops.geom()
			.eccentricity(ops.geom().contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testRoundness() {
		final double expected = expensiveTestsEnabled ? 0.598704192 : 0.591779852;
		assertEquals(Ops.Geometric.Roundness.NAME, expected, ops.geom().roundness(
			ops.geom().contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testCentroidMesh() {
		final double expected1 = expensiveTestsEnabled ? -25.700 : -12.237808234;
		final double expected2 = expensiveTestsEnabled ? -24.644 : -12.524262243;
		final double expected3 = expensiveTestsEnabled ? -19.945 : -9.568196449;
		final RealPoint c = (RealPoint) ops.geom().centroid(ops.geom()
			.marchingcubes(region3D));
		assertEquals(expected1, c.getDoublePosition(0),
			AbstractFeatureTest.BIG_DELTA);
		assertEquals(expected2, c.getDoublePosition(1),
			AbstractFeatureTest.BIG_DELTA);
		assertEquals(expected3, c.getDoublePosition(2),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSolidity2D() {
		final double expected = expensiveTestsEnabled ? 0.997063173 : 0.976990400;
		assertEquals(Ops.Geometric.Solidity.NAME, expected, ops.geom().solidity(ops
			.geom().contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSolidity3D() {
		final double expected = expensiveTestsEnabled ? 0.754 : 0.905805001;
		// This test is just here for completeness.
		// All input values of solidity are verified.
		assertEquals(Ops.Geometric.Solidity.NAME, expected, ops.geom().solidity(ops
			.geom().marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testRugosity() {
		final double expected = expensiveTestsEnabled ? 1.379 : 1.035052196;
		// This test is just here for completeness.
		// All input values of convexity are verified.
		assertEquals(Ops.Geometric.Rugosity.NAME, expected, ops.geom().rugosity(ops
			.geom().marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testConvexity() {
		final double expected = expensiveTestsEnabled ? 0.725 : 0.966134851;
		// This test is just here for completeness.
		// All input values of convexity are verified.
		assertEquals(Ops.Geometric.Convexity.NAME, expected, ops.geom().convexity(
			ops.geom().marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testBoundaryPixelCountConvexHull() {
		final double expected = expensiveTestsEnabled ? 32 : 177;
		// Verified by hand. qhull merges faces and therefore has another number
		// of surface pixels
		assertEquals(Ops.Geometric.BoundaryPixelCountConvexHull.NAME, expected, ops
			.geom().boundarypixelcountconvexhull(ops.geom().marchingcubes(region3D))
			.get(), AbstractFeatureTest.BIG_DELTA);

	}

	@Test
	public void testBoundarySizeConvexHull() {
		final double expected = expensiveTestsEnabled ? 13580.54 : 1457.276963378;
		// value taken from qhull (qhull.org)
		assertEquals(Ops.Geometric.BoundarySizeConvexHull.NAME, expected, ops.geom()
			.boundarysizeconvexhull(ops.geom().marchingcubes(region3D)).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSizeConvexHull() {
		final double expected = expensiveTestsEnabled ? 108660.667 : 4930.375000000;
		// value taken from qhull (qhull.org)
		assertEquals(Ops.Geometric.SizeConvexHull.NAME, expected, ops.geom()
			.sizeconvexhull(ops.geom().marchingcubes(region3D)).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testBoundarySize3D() {
		final double expected = expensiveTestsEnabled ? 18741.018 : 1508.357722350;
		// The delta is relatively big because they use float numbers in imagej
		// and my implementation is based on doubles.
		assertEquals(Ops.Geometric.BoundarySize.NAME, expected, ops.geom()
			.boundarysize(ops.geom().marchingcubes(region3D)).get(), 0.186);
	}

	@Test
	public void testBoundaryPixelCount() {
		final double expected = expensiveTestsEnabled ? 20996.0 : 2070.0;
		assertEquals(Ops.Geometric.BoundaryPixelCount.NAME, expected, ops.geom()
			.boundarypixelcount(ops.geom().marchingcubes(region3D)).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSize3D() {
		final double expected = expensiveTestsEnabled ? 81992 : 4488;
		assertEquals(Ops.Geometric.Size.NAME, expected, ops.geom().size(region3D)
			.get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testCompactness() {
		final double expected = expensiveTestsEnabled ? 0.082 : 0.254314235;
		assertEquals(Ops.Geometric.Compactness.NAME, expected, ops.geom()
			.compactness(ops.geom().marchingcubes(region3D)).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSphericity() {
		final double expected = expensiveTestsEnabled ? 0.435 : 0.633563608;
		assertEquals(Ops.Geometric.Sphericity.NAME, expected, ops.geom().sphericity(
			ops.geom().marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testMainElongation() {
		final double expected = expensiveTestsEnabled ? 1.041 : 1.270370555;
		assertEquals(Ops.Geometric.MainElongation.NAME, expected, ops.geom()
			.mainelongation(region3D).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testMedianElongation() {
		final double expected = expensiveTestsEnabled ? 1.225 : 1.137093214;
		assertEquals(Ops.Geometric.MedianElongation.NAME, expected, ops.geom()
			.medianelongation(region3D).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSpareness() {
		final double expected = expensiveTestsEnabled ? 0.509 : 0.978261746;
		assertEquals(Ops.Geometric.Spareness.NAME, expected, ops.geom().spareness(
			region3D).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testCenterOfGravity() {
		final double expected1 = expensiveTestsEnabled ? 396.063362 : 39.455323989;
		final double expected2 = expensiveTestsEnabled ? 576.763804 : 57.580063973;
		assertEquals(Ops.Geometric.CenterOfGravity.NAME, expected1, ops.geom()
			.centerofgravity(img2d).getDoublePosition(0),
			AbstractFeatureTest.BIG_DELTA);
		assertEquals(Ops.Geometric.CenterOfGravity.NAME, expected2, ops.geom()
			.centerofgravity(img2d).getDoublePosition(1),
			AbstractFeatureTest.BIG_DELTA);
	}

}
