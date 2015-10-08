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

import net.imagej.ops.Ops.Geometric.BoundaryPixel;
import net.imagej.ops.Ops.Geometric.BoundaryPixelConvexHull;
import net.imagej.ops.Ops.Geometric.BoundarySize;
import net.imagej.ops.Ops.Geometric.BoundarySizeConvexHull;
import net.imagej.ops.Ops.Geometric.Circularity;
import net.imagej.ops.Ops.Geometric.Compactness;
import net.imagej.ops.Ops.Geometric.Convexity;
import net.imagej.ops.Ops.Geometric.Eccentricity;
import net.imagej.ops.Ops.Geometric.FeretsAngle;
import net.imagej.ops.Ops.Geometric.FeretsDiameter;
import net.imagej.ops.Ops.Geometric.MainElongation;
import net.imagej.ops.Ops.Geometric.MajorAxis;
import net.imagej.ops.Ops.Geometric.MedianElongation;
import net.imagej.ops.Ops.Geometric.MinorAxis;
import net.imagej.ops.Ops.Geometric.Roundness;
import net.imagej.ops.Ops.Geometric.Rugosity;
import net.imagej.ops.Ops.Geometric.Size;
import net.imagej.ops.Ops.Geometric.SizeConvexHull;
import net.imagej.ops.Ops.Geometric.Solidity;
import net.imagej.ops.Ops.Geometric.Spareness;
import net.imagej.ops.Ops.Geometric.Sphericity;
import net.imagej.ops.features.AbstractFeatureTest;
import net.imglib2.RandomAccess;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.roi.labeling.ImgLabeling;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.labeling.LabelRegions;
import net.imglib2.roi.labeling.LabelingType;
import net.imglib2.type.numeric.integer.IntType;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * Tests for geom features
 * 
 * @author Daniel Seebacher, University of Konstanz
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class GeomTest extends AbstractFeatureTest {

	private static LabelRegion<String> region2D;
	private static LabelRegion<String> region3D;

	@BeforeClass
	public static void setupBefore() throws MalformedURLException, IOException {
		// read simple polygon image
		final BufferedImage read = ImageIO.read(GeomTest.class
				.getResourceAsStream("cZgkFsK.png"));

		final ImgLabeling<String, IntType> img = new ImgLabeling<String, IntType>(
				ArrayImgs.ints(read.getWidth(), read.getHeight()));

		// at each black pixel of the polygon add a "1" label.
		final RandomAccess<LabelingType<String>> randomAccess = img
				.randomAccess();
		for (int y = 0; y < read.getHeight(); y++) {
			for (int x = 0; x < read.getWidth(); x++) {
				randomAccess.setPosition(new int[] { x, y });
				final Color c = new Color(read.getRGB(x, y));
				if (c.getRed() == Color.black.getRed()) {
					randomAccess.get().add("1");
				}
			}
		}

		final LabelRegions<String> labelRegions = new LabelRegions<String>(img);
		region2D = labelRegions.getLabelRegion("1");
		
		try {
			region3D = createLabelRegion3D();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void createPolygon() {
		ops.geom().contour(region2D, true, true);
	}

	/**
	 * Test the {@link Area} Op.
	 */
	@Test
	public void testArea() {
		// value taken from imagej
		assertEquals(
				Size.NAME,
				355630.5,
				ops.geom()
						.size(ops.geom().contour(region2D, true, true))
						.getRealDouble(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link Perimeter} Op.
	 */
	@Test
	public void testPerimeter() {
		// value taken from imagej
		assertEquals(BoundarySize.NAME, 2658.990257670, ops.geom()
				.boundarysize(ops.geom().contour(region2D, true, true))
				.getRealDouble(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link Circularity} Op.
	 */
	@Test
	public void testCircularity() {
		// value taken from imagej
		assertEquals(Circularity.NAME, 0.632083948, ops.geom()
				.circularity(ops.geom().contour(region2D, true, true))
				.getRealDouble(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link MinorAxis} Op.
	 */
	@Test
	public void testMinorAxis() {
		// value taken from imagej
		assertEquals(MinorAxis.NAME, 520.667420750, ops.geom()
				.minoraxis(ops.geom().contour(region2D, true, true))
				.getRealDouble(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link MajorAxis} Op.
	 */
	@Test
	public void testMajorAxis() {
		// value taken from imagej
		assertEquals(MajorAxis.NAME, 869.657215429, ops.geom()
				.majoraxis(ops.geom().contour(region2D, true, true))
				.getRealDouble(), 0.01);
	}

	/**
	 * Test the {@link FeretsDiameter} Op.
	 */
	@Test
	public void testFeretDiameter() {
		// value taken from imagej
		assertEquals(FeretsDiameter.NAME, 908.002202641, ops.geom()
				.feretsdiameter(ops.geom().contour(region2D, true, true))
				.getRealDouble(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link FeretsAngle} Op.
	 */
	@Test
	public void testFeretAngle() {

		// value taken from imagej, angle could be reversed so check
		// 148.235410152 and
		// 148.235410152.. + 180
		final double expectedAngle = 148.235410152;
		final double actualAngle = ops.geom()
				.feretsangle(ops.geom().contour(region2D, true, true))
				.getRealDouble();

		boolean isEquals = false;
		if (Math.abs(expectedAngle - actualAngle) < AbstractFeatureTest.SMALL_DELTA
				|| Math.abs(expectedAngle + 180 - actualAngle) < AbstractFeatureTest.SMALL_DELTA) {
			isEquals = true;
		}

		assertTrue(FeretsAngle.NAME + " Expected [" + expectedAngle + "] was ["
				+ actualAngle + "]", isEquals);
	}

	/**
	 * Test the {@link Eccentricity} Op.
	 */
	@Test
	public void testEccentricity() {
		// value taken from imagej
		assertEquals(Eccentricity.NAME, 1.670273923, ops.geom()
				.eccentricity(ops.geom().contour(region2D, true, true))
				.getRealDouble(), AbstractFeatureTest.BIG_DELTA);

	}

	/**
	 * Test the {@link Roundness} Op.
	 */
	@Test
	public void testRoundness() {
		// value taken from imagej
		assertEquals(
				Roundness.NAME,
				0.598704192,
				ops.geom()
						.roundness(
								ops.geom().contour(region2D, true, true))
						.getRealDouble(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link Solidity} Op.
	 */
	@Test
	public void testSolidity2D() {
		// value taken from imagej
		assertEquals(
				Solidity.NAME,
				0.997063173,
				ops.geom()
						.solidity(ops.geom().contour(region2D, true, true))
						.getRealDouble(), AbstractFeatureTest.BIG_DELTA);
	}
	
	/**
	 * Test the {@link Solidity} Op.
	 */
	@Test
	public void testSolidity3D() {
		// This test is just here for completeness.
		// All input values of solidity are verified.
		assertEquals(Solidity.NAME, 0.902, ops.geom()
				.solidity(ops.geom().marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link Rugosity} Op.
	 */
	@Test
	public void testRugosity() {
		// This test is just here for completeness.
		// All input values of convexity are verified.
		assertEquals(Rugosity.NAME, 1.099, ops.geom()
				.rugosity(ops.geom().marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link Convexity} Op.
	 */
	@Test
	public void testConvexity() {
		// This test is just here for completeness.
		// All input values of convexity are verified.
		assertEquals(Convexity.NAME, 0.910, ops.geom()
				.convexity(ops.geom().marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link BoundaryPixelConvexHull} Op.
	 */
	@Test
	public void testConvexHullSurfacePixel() {
		// Verified by hand. qhull merges faces and therefore has another number
		// of surface pixels
		assertEquals(BoundaryPixelConvexHull.NAME, 585, ops
				.geom().boundarypixelconvexhull(ops.geom().marchingcubes(region3D)).get(),
				AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link BoundarySizeConvexHull} Op.
	 */
	@Test
	public void testConvexHullSurfaceArea() {
		// value taken from qhull (qhull.org)
		assertEquals(BoundarySizeConvexHull.NAME, 19133.663, ops
				.geom().boundarysizeconvexhull(ops.geom().marchingcubes(region3D)).get(),
				AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link SizeConvexHull} Op.
	 */
	@Test
	public void testConvexHullVolume() {
		// value taken from qhull (qhull.org)
		assertEquals(SizeConvexHull.NAME, 234284.5, ops
				.geom().sizeconvexhull(ops.geom().marchingcubes(region3D)).get(),
				AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link BoundarySize} Op.
	 */
	@Test
	public void testSurfaceArea() {
		// value taken from imagej
		// The delta is relatively big because they use float numbers in imagej
		// and my implementation is based on doubles.
		assertEquals(BoundarySize.NAME, 21025.018, ops.geom()
				.boundarysize(ops.geom().marchingcubes(region3D)).get(), 0.186);
	}

	/**
	 * Test the {@link BoundaryPixel} Op.
	 */
	@Test
	public void testSurfacePixel() {
		// value taken from imagej
		assertEquals(BoundaryPixel.NAME, 29738, ops.geom()
				.boundarypixel(ops.geom().marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link Size} Op.
	 */
	@Test
	public void testVolume() {
		// value taken from imagej
		assertEquals(Size.NAME, 211296,
				ops.geom().size(region3D).get(),
				AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link Compactness} Op.
	 */
	@Test
	public void testCompactness() {
		// value taken from imagej
		assertEquals(Compactness.NAME, 0.192, ops.geom()
				.compactness(ops.geom().marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link Sphericity} Op.
	 */
	@Test
	public void testSphericity() {
		// value taken from imagej
		assertEquals(Sphericity.NAME, 0.577, ops.geom()
				.sphericity(ops.geom().marchingcubes(region3D)).get(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link MainElongation} Op.
	 */
	@Test
	public void testMainElongation() {
		// value taken from imagej
		assertEquals(MainElongation.NAME, 1.312, ops.geom()
				.mainelongation(region3D).get(), AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link MedianElongation} Op.
	 */
	@Test
	public void testMedianElongation() {
		// value taken from imagej
		assertEquals(MedianElongation.NAME, 1.126, ops
				.geom().medianelongation(region3D).get(),
				AbstractFeatureTest.BIG_DELTA);
	}

	/**
	 * Test the {@link Spareness} Op.
	 */
	@Test
	public void testSpareness() {
		// value taken from imagej
		assertEquals(Spareness.NAME, 0.970, ops.geom()
				.spareness(region3D).get(), AbstractFeatureTest.BIG_DELTA);
	}
}
