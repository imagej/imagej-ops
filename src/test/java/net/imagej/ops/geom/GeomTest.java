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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;

import net.imagej.ops.Ops;
import net.imagej.ops.features.AbstractFeatureTest;
import net.imglib2.RandomAccess;
import net.imglib2.RealPoint;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.numeric.integer.IntType;
import net.imglib2.type.numeric.real.FloatType;

import org.junit.BeforeClass;
import org.junit.Test;

import ij.io.Opener;

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

	@Test
	public void createPolygon() {
		ops.geom().contour(region2D, true, true);
	}

	@Test
	public void testSize() {
		assertEquals(Ops.Geometric.Size.NAME, 355630.5, ops.geom().size(ops.geom().contour(
			region2D, true, true)).getRealDouble(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testBoundarySize2D() {
		assertEquals(Ops.Geometric.BoundarySize.NAME, 2658.990257670, ops.geom().boundarysize(ops
			.geom().contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testCircularity() {
		assertEquals(Ops.Geometric.Circularity.NAME, 0.632083948, ops.geom().circularity(ops
			.geom().contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testMinorAxis() {
		assertEquals(Ops.Geometric.MinorAxis.NAME, 520.667420750, ops.geom().minoraxis(ops.geom()
			.contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testMajorAxis() {
		assertEquals(Ops.Geometric.MajorAxis.NAME, 869.657215429, ops.geom().majoraxis(ops.geom()
			.contour(region2D, true, true)).getRealDouble(), 0.01);
	}

	@Test
	public void testFeretsDiameter() {
		assertEquals(Ops.Geometric.FeretsDiameter.NAME, 908.002202641, ops.geom().feretsdiameter(
			ops.geom().contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testFeretsAngle() {
		// angle could be reversed so check
		// 148.235410152 and
		// 148.235410152.. + 180
		final double expectedAngle = 148.235410152;
		final double actualAngle = ops.geom().feretsangle(ops.geom().contour(
			region2D, true, true)).getRealDouble();

		boolean isEquals = false;
		if (Math.abs(expectedAngle -
			actualAngle) < AbstractFeatureTest.SMALL_DELTA || Math.abs(expectedAngle +
				180 - actualAngle) < AbstractFeatureTest.SMALL_DELTA)
		{
			isEquals = true;
		}

		assertTrue(Ops.Geometric.FeretsAngle.NAME + " Expected [" + expectedAngle + "] was [" +
			actualAngle + "]", isEquals);
	}

	@Test
	public void testEccentricity() {
		assertEquals(Ops.Geometric.Eccentricity.NAME, 1.670273923, ops.geom().eccentricity(ops
			.geom().contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testRoundness() {
		assertEquals(Ops.Geometric.Roundness.NAME, 0.598704192, ops.geom().roundness(ops.geom()
			.contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}
	
	@Test
	public void testCentroidMesh() {
		RealPoint c = (RealPoint) ops.geom().centroid(ops.geom().marchingcubes(region3D));
		assertEquals(-25.700, c.getDoublePosition(0), AbstractFeatureTest.BIG_DELTA);
		assertEquals(-24.644, c.getDoublePosition(1), AbstractFeatureTest.BIG_DELTA);
		assertEquals(-19.945, c.getDoublePosition(2), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSolidity2D() {
		assertEquals(Ops.Geometric.Solidity.NAME, 0.997063173, ops.geom().solidity(ops.geom()
			.contour(region2D, true, true)).getRealDouble(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSolidity3D() {
		// This test is just here for completeness.
		// All input values of solidity are verified.
		assertEquals(Ops.Geometric.Solidity.NAME, 0.754, ops.geom().solidity(ops.geom()
			.marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testRugosity() {
		// This test is just here for completeness.
		// All input values of convexity are verified.
		assertEquals(Ops.Geometric.Rugosity.NAME, 1.379, ops.geom().rugosity(ops.geom()
			.marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testConvexity() {
		// This test is just here for completeness.
		// All input values of convexity are verified.
		assertEquals(Ops.Geometric.Convexity.NAME, 0.725, ops.geom().convexity(ops.geom()
			.marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testBoundaryPixelCountConvexHull() {
		// Verified by hand. qhull merges faces and therefore has another number
		// of surface pixels
		assertEquals(Ops.Geometric.BoundaryPixelCountConvexHull.NAME, 32, ops.geom()
			.boundarypixelcountconvexhull(ops.geom().marchingcubes(region3D)).get(),
			AbstractFeatureTest.BIG_DELTA);

	}

	@Test
	public void testBoundarySizeConvexHull() {
		// value taken from qhull (qhull.org)
		assertEquals(Ops.Geometric.BoundarySizeConvexHull.NAME, 13580.54, ops.geom()
			.boundarysizeconvexhull(ops.geom().marchingcubes(region3D)).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSizeConvexHull() {
		// value taken from qhull (qhull.org)
		assertEquals(Ops.Geometric.SizeConvexHull.NAME, 108660.667, ops.geom().sizeconvexhull(ops
			.geom().marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testBoundarySize3D() {
		// The delta is relatively big because they use float numbers in imagej
		// and my implementation is based on doubles.
		assertEquals(Ops.Geometric.BoundarySize.NAME, 18741.018, ops.geom().boundarysize(ops
			.geom().marchingcubes(region3D)).get(), 0.186);
	}

	@Test
	public void testBoundaryPixelCount() {
		assertEquals(Ops.Geometric.BoundaryPixelCount.NAME, 20996.0, ops.geom()
			.boundarypixelcount(ops.geom().marchingcubes(region3D)).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSize3D() {
		assertEquals(Ops.Geometric.Size.NAME, 81992, ops.geom().size(region3D).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testCompactness() {
		assertEquals(Ops.Geometric.Compactness.NAME, 0.082, ops.geom().compactness(ops.geom()
			.marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSphericity() {
		assertEquals(Ops.Geometric.Sphericity.NAME, 0.435, ops.geom().sphericity(ops.geom()
			.marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testMainElongation() {
		assertEquals(Ops.Geometric.MainElongation.NAME, 1.041, ops.geom().mainelongation(region3D)
			.get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testMedianElongation() {
		assertEquals(Ops.Geometric.MedianElongation.NAME, 1.225, ops.geom().medianelongation(
			region3D).get(), AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testSpareness() {
		assertEquals(Ops.Geometric.Spareness.NAME, 0.509, ops.geom().spareness(region3D).get(),
			AbstractFeatureTest.BIG_DELTA);
	}

	@Test
	public void testCenterOfGravity() {
		assertEquals(Ops.Geometric.CenterOfGravity.NAME, 396.063362,
				ops.geom().centerofgravity(img2d).getDoublePosition(0), AbstractFeatureTest.BIG_DELTA);
		assertEquals(Ops.Geometric.CenterOfGravity.NAME, 576.763804,
				ops.geom().centerofgravity(img2d).getDoublePosition(1), AbstractFeatureTest.BIG_DELTA);
	}

}
