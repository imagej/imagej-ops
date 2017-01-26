/*-
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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

import java.util.List;

import net.imagej.ops.Ops;
import net.imagej.ops.features.AbstractFeatureTest;
import net.imagej.ops.geom.geom3d.DefaultBoxivityMesh;
import net.imagej.ops.geom.geom3d.DefaultCompactness;
import net.imagej.ops.geom.geom3d.DefaultConvexityMesh;
import net.imagej.ops.geom.geom3d.DefaultMainElongation;
import net.imagej.ops.geom.geom3d.DefaultMarchingCubes;
import net.imagej.ops.geom.geom3d.DefaultMedianElongation;
import net.imagej.ops.geom.geom3d.DefaultSolidityMesh;
import net.imagej.ops.geom.geom3d.DefaultSparenessMesh;
import net.imagej.ops.geom.geom3d.DefaultSphericity;
import net.imagej.ops.geom.geom3d.DefaultSurfaceArea;
import net.imagej.ops.geom.geom3d.DefaultSurfaceAreaConvexHullMesh;
import net.imagej.ops.geom.geom3d.DefaultVerticesCountConvexHullMesh;
import net.imagej.ops.geom.geom3d.DefaultVerticesCountMesh;
import net.imagej.ops.geom.geom3d.DefaultVolumeConvexHullMesh;
import net.imagej.ops.geom.geom3d.DefaultVolumeMesh;
import net.imagej.ops.geom.geom3d.mesh.DefaultMesh;
import net.imagej.ops.geom.geom3d.mesh.Facet;
import net.imagej.ops.geom.geom3d.mesh.Mesh;
import net.imagej.ops.geom.geom3d.mesh.TriangularFacet;
import net.imagej.ops.geom.geom3d.mesh.Vertex;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.numeric.real.DoubleType;

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
		assertEquals(Ops.Geometric.Convexity.NAME, 0.251596105039,
				((DoubleType) ops.run(DefaultMainElongation.class, mesh)).get(), EPSILON);
	}

	@Test
	public void marchingCubes() {
		final DefaultMesh result = (DefaultMesh) ops.run(DefaultMarchingCubes.class, ROI);
		final List<Facet> expectedFacets = mesh.getFacets();
		final List<Facet> resultFacets = result.getFacets();
		for (int i = 0; i < expectedFacets.size(); i++) {
			final TriangularFacet tmpR = (TriangularFacet) resultFacets.get(i);
			final TriangularFacet tmpE = (TriangularFacet) expectedFacets.get(i);

			for (int j = 0; j < 3; j++) {
				final Vertex resultVertex = tmpR.getVertex(j);
				final Vertex expectedVertex = tmpE.getVertex(j);
				assertEquals("Triangular Facet point " + j + " differes in x- coordinate:",
						expectedVertex.getDoublePosition(0), resultVertex.getDoublePosition(0), EPSILON);
				assertEquals("Triangular Facet point " + j + " differes in y- coordinate:",
						expectedVertex.getDoublePosition(1), resultVertex.getDoublePosition(1), EPSILON);
				assertEquals("Triangular Facet point " + j + " differes in z- coordinate:",
						expectedVertex.getDoublePosition(2), resultVertex.getDoublePosition(2), EPSILON);
			}
		}
	}

	@Test
	public void medianElongation() {
		// formula verified and ground truth computed with matlab
		assertEquals(Ops.Geometric.Convexity.NAME, 0.128824753606,
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
		assertEquals(Ops.Geometric.Spareness.NAME, 0.983875774303,
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

	@Test
	public void voxelization3D() {
		// https://github.com/imagej/imagej-ops/issues/422
	}
}
