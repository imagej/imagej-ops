/*-
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2023 ImageJ2 developers.
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

import java.util.Iterator;

import net.imagej.mesh.Mesh;
import net.imagej.mesh.Triangle;
import net.imagej.ops.Ops;
import net.imagej.ops.features.AbstractFeatureTest;
import net.imagej.ops.geom.geom3d.*;
import net.imagej.ops.morphology.fillHoles.DefaultFillHoles;
import net.imagej.ops.morphology.floodFill.DefaultFloodFill;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.real.DoubleType;

import net.imglib2.view.Views;
import org.junit.BeforeClass;
import org.junit.Test;

public class MeshFeatureTests extends AbstractFeatureTest {
	private static final double EPSILON = 10e-12;
	private static LabelRegion<String> ROI;
	private static Mesh mesh;

	@BeforeClass
	public static void setupBefore() {
		ROI = createLabelRegion(getTestImage3D(), 1, 255);
		mesh = getMesh();
	}

	@Test
	public void boxivityMesh() {
		try {
			ops.run(DefaultBoxivityMesh.class, mesh);
		} catch (IllegalArgumentException e) {
			// DefaultSmallestOrientedBoundingBox is not implemented.
		}
	}

	@Test
	public void compactness() {
		// formula verified and ground truth computed with matlab
		assertEquals(Ops.Geometric.Compactness.NAME, 0.572416357359835,
				((DoubleType) ops.run(DefaultCompactness.class, mesh)).get(), EPSILON);
	}

	@Test
	public void convexHull3D() {
		/**
		 * convexHull3D is tested in {@link QuickHull3DTest}.
		 */
	}

	@Test
	public void convexityMesh() {
		// formula verified and ground truth computed with matlab
		assertEquals(Ops.Geometric.Convexity.NAME, 0.983930494866521,
				((DoubleType) ops.run(DefaultConvexityMesh.class, mesh)).get(), EPSILON);
	}

	@Test
	public void mainElongation() {
		// formula verified and ground truth computed with matlab
		assertEquals(Ops.Geometric.Convexity.NAME, 0.2079585956045953,
				((DoubleType) ops.run(DefaultMainElongation.class, mesh)).get(), EPSILON);
	}

	@Test
	public void marchingCubes() {
		final Mesh result = (Mesh) ops.run(DefaultMarchingCubes.class, ROI);
		assertEquals(mesh.triangles().size(), result.triangles().size());
		final Iterator<Triangle> expectedFacets = mesh.triangles().iterator();
		final Iterator<Triangle> actualFacets = result.triangles().iterator();
		while (expectedFacets.hasNext() && actualFacets.hasNext()) {
			final Triangle expected = expectedFacets.next();
			final Triangle actual = actualFacets.next();
			assertEquals(expected.v0x(), actual.v0x(), EPSILON);
			assertEquals(expected.v0y(), actual.v0y(), EPSILON);
			assertEquals(expected.v0z(), actual.v0z(), EPSILON);
			assertEquals(expected.v1x(), actual.v1x(), EPSILON);
			assertEquals(expected.v1y(), actual.v1y(), EPSILON);
			assertEquals(expected.v1z(), actual.v1z(), EPSILON);
			assertEquals(expected.v2x(), actual.v2x(), EPSILON);
			assertEquals(expected.v2y(), actual.v2y(), EPSILON);
			assertEquals(expected.v2z(), actual.v2z(), EPSILON);
		}
		assertTrue(!expectedFacets.hasNext() && !actualFacets.hasNext());
	}

	@Test
	public void medianElongation() {
		// formula verified and ground truth computed with matlab
		assertEquals(Ops.Geometric.Convexity.NAME, 0.30059118825775455,
				((DoubleType) ops.run(DefaultMedianElongation.class, mesh)).get(), EPSILON);
	}

	@Test
	public void sizeConvexHullMesh() {
		// ground truth computed with matlab
		assertEquals(Ops.Geometric.SizeConvexHull.NAME, 304.5,
				((DoubleType) ops.run(DefaultVolumeConvexHullMesh.class, mesh)).get(), EPSILON);
	}

	@Test
	public void sizeMesh() {
		// ground truth computed with matlab
		assertEquals(Ops.Geometric.Size.NAME, 257.5, ((DoubleType) ops.run(DefaultVolumeMesh.class, mesh)).get(),
				EPSILON);
	}

	@Test
	public void solidityMesh() {
		// formula verified and ground truth computed with matlab
		assertEquals(Ops.Geometric.Solidity.NAME, 0.845648604269294,
				((DoubleType) ops.run(DefaultSolidityMesh.class, mesh)).get(), EPSILON);
	}

	@Test
	public void spareness() {
		// formula verified
		assertEquals(Ops.Geometric.Spareness.NAME, 0.7884710437076516,
				((DoubleType) ops.run(DefaultSparenessMesh.class, mesh)).get(), EPSILON);
	}

	@Test
	public void sphericity() {
		// formula verified and ground truth computed with matlab
		assertEquals(Ops.Geometric.Sphericity.NAME, 0.830304411183464,
				((DoubleType) ops.run(DefaultSphericity.class, mesh)).get(), EPSILON);
	}

	@Test
	public void surfaceArea() {
		// ground truth computed with matlab
		assertEquals(Ops.Geometric.BoundarySize.NAME, 235.7390893402464,
				((DoubleType) ops.run(DefaultSurfaceArea.class, mesh)).get(), EPSILON);
	}

	@Test
	public void surfaceAreaConvexHull() {
		// ground truth computed with matlab
		assertEquals(Ops.Geometric.BoundarySize.NAME, 231.9508788339317,
				((DoubleType) ops.run(DefaultSurfaceAreaConvexHullMesh.class, mesh)).get(), EPSILON);
	}

	@Test
	public void verticesCountConvexHullMesh() {
		// verified with matlab
		assertEquals(Ops.Geometric.VerticesCountConvexHull.NAME, 57,
				((DoubleType) ops.run(DefaultVerticesCountConvexHullMesh.class, mesh)).get(), EPSILON);
	}

	@Test
	public void verticesCountMesh() {
		// verified with matlab
		assertEquals(Ops.Geometric.VerticesCountConvexHull.NAME, 184,
				((DoubleType) ops.run(DefaultVerticesCountMesh.class, mesh)).get(), EPSILON);

	}

	/**
	 * Creates a 3D binary image of a sphere.
	 *
	 * @param r The radius of the sphere.
	 * @return A RandomAccessibleInterval representing the sphere.
	 */
	public RandomAccessibleInterval<BitType> generateSphere(int r) {
		long[] dims = new long[] {2*r, 2*r, 2*r}; // Dimensions of the bounding box of the sphere
		Img<BitType> sphereImg = ArrayImgs.bits(dims);

		Cursor<BitType> cursor = sphereImg.localizingCursor();

		// Center of the sphere
		int cx = r;
		int cy = r;
		int cz = r;

		while (cursor.hasNext()) {
			cursor.fwd();
			int x = cursor.getIntPosition(0) - cx;
			int y = cursor.getIntPosition(1) - cy;
			int z = cursor.getIntPosition(2) - cz;

			if (x * x + y * y + z * z <= r * r) {
				cursor.get().set(true);
			}
		}

		return sphereImg;
	}

	public long compareImages(RandomAccessibleInterval<BitType> img1, RandomAccessibleInterval<BitType> img2) {
		long diff = 0;
		Cursor<BitType> cursor1 = Views.iterable(img1).cursor();
		Cursor<BitType> cursor2 = Views.iterable(img2).cursor();

		while (cursor1.hasNext() && cursor2.hasNext()) {
			cursor1.fwd();
			cursor2.fwd();

			if (!cursor1.get().valueEquals(cursor2.get())) {
				diff++;
			}
		}
		return diff;
	}

	@Test
	public void voxelization3D() {
		// https://github.com/imagej/imagej-ops/issues/422
		RandomAccessibleInterval<BitType> sphere = generateSphere(20);
		final Mesh result = (Mesh) ops.run(DefaultMarchingCubes.class, sphere);

		// The mesh is good by now, let's check the voxelization
		RandomAccessibleInterval<BitType> voxelization = (RandomAccessibleInterval<BitType>) ops.run(DefaultVoxelization3D.class, result);

		// Flood fill (ops implementation starts from borders)
		RandomAccessibleInterval<BitType> filledVoxelization = (RandomAccessibleInterval<BitType>) ops.run(DefaultFillHoles.class, voxelization);

		// Compare inverted image
		// Comparison
		long diff = compareImages(sphere, filledVoxelization);
		long total = ROI.size();

		assertTrue("Voxelization does not match the original image closely enough.", diff / (double) total < 0.15);

	}
}
