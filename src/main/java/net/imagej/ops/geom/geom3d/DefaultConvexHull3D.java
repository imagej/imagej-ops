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

package net.imagej.ops.geom.geom3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.imagej.mesh.Mesh;
import net.imagej.mesh.naive.NaiveDoubleMesh;
import net.imagej.ops.Ops;
import net.imagej.ops.geom.geom3d.mesh.Horizon;
import net.imagej.ops.geom.geom3d.mesh.TriangularFacet;
import net.imagej.ops.geom.geom3d.mesh.Vertex;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This quickhull implementation is based on the paper
 * "The Quickhull Algorithm for Convex Hulls" by Barber, Dobkin and Huhdanpaa
 * (http://dpd.cs.princeton.edu/Papers/BarberDobkinHuhdanpaa.pdf). The
 * computation of the initial simplex is inspired by John Lloyd's quickhull
 * implementation (http://www.cs.ubc.ca/~lloyd/java/quickhull3d.html).
 * 
 * @author Tim-Oliver Buchholz (University of Konstanz)
 */
@Plugin(type = Ops.Geometric.ConvexHull.class)
public class DefaultConvexHull3D extends AbstractUnaryFunctionOp<Mesh, Mesh>
	implements Ops.Geometric.ConvexHull
{

	@Parameter(type = ItemIO.OUTPUT)
	private double epsilon;

	/**
	 * Precision of a double.
	 */
	private final double DOUBLE_PREC = 2.2204460492503131e-16;

	/** Gets the epsilon value of the last executed calculation. */
	public double getEpsilon() {
		return epsilon;
	}

	@Override
	public Mesh calculate(final Mesh input) {
		Mesh output = new NaiveDoubleMesh();
		Set<Vertex> vertices = new LinkedHashSet<>();
		for (final net.imagej.mesh.Vertex v : input.vertices()) {
			final Vertex vertex = new Vertex(v.x(), v.y(), v.z());
			vertices.add(vertex);
		}
		List<TriangularFacet> facets = new ArrayList<>();
		List<TriangularFacet> facetsWithPointInFront = new ArrayList<>();
		epsilon = computeHull(vertices, facets, facetsWithPointInFront);

		final Map<Vertex, Long> vertexIndices = new HashMap<>();
		for (final TriangularFacet f : facets) {
			final Vertex v0 = f.getP0();
			final Vertex v1 = f.getP1();
			final Vertex v2 = f.getP2();
			final long vIndex0 = vertexIndices.computeIfAbsent(v0, //
				v -> output.vertices().add(v0.getX(), v0.getY(), v0.getZ()));
			final long vIndex1 = vertexIndices.computeIfAbsent(v1, //
				v -> output.vertices().add(v1.getX(), v1.getY(), v1.getZ()));
			final long vIndex2 = vertexIndices.computeIfAbsent(v2, //
				v -> output.vertices().add(v2.getX(), v2.getY(), v2.getZ()));
			final Vector3D normal = f.getNormal();
			final double nx = normal.getX();
			final double ny = normal.getY();
			final double nz = normal.getZ();
			output.triangles().add(vIndex0, vIndex1, vIndex2, nx, ny, nz);
		}
		return output;
	}

	/**
	 * Compute the convex hull.
	 * 
	 * @param facetsWithPointInFront
	 */
	private double computeHull(final Set<Vertex> vertices,
		final List<TriangularFacet> facets,
		final List<TriangularFacet> facetsWithPointInFront)
	{
		final double eps = createSimplex(vertices, facets,
			facetsWithPointInFront);
		while (!facetsWithPointInFront.isEmpty()) {
			replaceFacet(eps, vertices, facets, facetsWithPointInFront,
				facetsWithPointInFront.remove(0));
		}

		return eps;
	}

	/**
	 * Replaces a facet with at least three new facets.
	 * 
	 * @param facet the facet to replace. At least one point must be in front of
	 *          next.
	 */
	private void replaceFacet(final double eps, final Set<Vertex> vertices,
		final List<TriangularFacet> facets,
		final List<TriangularFacet> facetsPointInFront,
		final TriangularFacet facet)
	{
		final Vertex v = facet.getMaximumDistanceVertex();
		final Horizon horizon = computeHorizon(eps, vertices, facets,
			facetsPointInFront, facet, v);
		assignPointsToFacets(eps, vertices, createFacets(horizon, v), facets,
			facetsPointInFront);
	}

	/**
	 * Adds for each edge of the horizon and vTop a new facet.
	 * 
	 * @param horizon facet of all facets seen from point vTop
	 * @param vTop point which is added to the convex hull
	 * @return new created facets
	 */
	private List<TriangularFacet> createFacets(final Horizon horizon,
		final Vertex vTop)
	{
		List<TriangularFacet> newFacets = new ArrayList<>();
		Vertex vLeft, vRight;

		// triangles 1 to n
		for (int i = 1; i < horizon.size(); i++) {
			vLeft = horizon.getVertex(i - 1);
			vRight = horizon.getVertex(i);

			TriangularFacet f = new TriangularFacet(vRight, vTop, vLeft);

			setNeighborZero(f, horizon.getNeighbor(i));

			newFacets.add(f);
		}

		// triangle 0, this triangle connects the n-th triangle with
		// triangle number 1
		vRight = horizon.getVertex(0);
		vLeft = horizon.getLastVertex();

		TriangularFacet f = new TriangularFacet(vRight, vTop, vLeft);

		setNeighborZero(f, horizon.getNeighbor(0));

		newFacets.add(f);

		// set neighbors 1 and 2 of each triangle
		// triangle 0 has triangle n and 1 as neighbors.
		connectTriangles(newFacets);
		return newFacets;
	}

	/**
	 * Sets neighbors for each new triangle. The triangles build a cone.
	 * 
	 * @param newFacets the triangles
	 */
	private void connectTriangles(final List<TriangularFacet> newFacets) {
		int lastFacetIndex = newFacets.size() - 1;
		for (int i = 1; i < lastFacetIndex; i++) {
			newFacets.get(i).setNeighbor(1, newFacets.get(i + 1));
			newFacets.get(i).setNeighbor(2, newFacets.get(i - 1));
		}
		newFacets.get(0).setNeighbor(1, newFacets.get(1));
		newFacets.get(0).setNeighbor(2, newFacets.get(lastFacetIndex));
		newFacets.get(lastFacetIndex).setNeighbor(1, newFacets.get(0));
		newFacets.get(lastFacetIndex).setNeighbor(2, newFacets.get(newFacets
			.size() - 2));
	}

	/**
	 * Sets the first neighbor of a new created triangle.
	 * 
	 * @param f the new facet.
	 * @param n the neighbor facet.
	 */
	private void setNeighborZero(final TriangularFacet f,
		final TriangularFacet n)
	{
		int vertexIndex = n.indexOfVertex(f.getVertex(2));
		n.replaceNeighbor(vertexIndex, f);

		f.setNeighbor(0, n);
	}

	/**
	 * Computes the horizon of vTop. The horizon is the merged facet of all facets
	 * which are in front of the point vTop.
	 * 
	 * @param frontFacet a face which is in front of vTop
	 * @param vTop a point outside of the convex hull
	 * @return facet containing all facets which are in front of vTop
	 */
	private Horizon computeHorizon(final double eps,
		final Set<Vertex> vertices, final List<TriangularFacet> facets,
		final List<TriangularFacet> facetsWithPointInFront,
		final TriangularFacet frontFacet, final Vertex vTop)
	{
		// Points which are in front have to be reassigned after all new facets
		// are constructed.
		vertices.addAll(frontFacet.getVerticesInFront());

		// frontFacet is not a result facet. Remove it from result list.
		facets.remove(frontFacet);

		Horizon h = new Horizon(frontFacet);
		TriangularFacet merge = nextFacetToMerge(eps, h, vTop);
		while (merge != null) {
			// This points have to be reassigned as well.
			vertices.addAll(merge.getVerticesInFront());
			// This face has some points in front and therefore is not a result
			// face.
			facets.remove(merge);
			// After this step this facet is merged with another facet.
			facetsWithPointInFront.remove(merge);

			if (h.containsAll(merge.getVertices())) {
				updateNeighbors(frontFacet, merge);
				h.complexMerge(merge);
			}
			else {
				updateNeighbors(frontFacet, merge);
				h.simpleMerge(merge);
			}
			merge = nextFacetToMerge(eps, h, vTop);
		}

		return h;
	}

	/**
	 * After the merge step the facet merge is part of frontFacet. Therefore the
	 * neighbors of merge must point to frontFacet and not to merge.
	 * 
	 * @param frontFacet the facet to which merge will be added.
	 * @param merge the facet which will be merged with frontFacet.
	 */
	private void updateNeighbors(final TriangularFacet frontFacet,
		final TriangularFacet merge)
	{
		for (TriangularFacet f : merge.getNeighbors()) {
			if (!f.equals(frontFacet)) {
				f.replaceNeighbor(f.indexOfNeighbor(merge), frontFacet);
			}
		}
	}

	/**
	 * Returns a facet which is in front of vTop and neighbor of front.
	 * 
	 * @param frontFacet facet in front of vTop
	 * @param vTop point which is added to the convex hull
	 * @return neighboring facet of front or null if no facet is in front
	 */
	private TriangularFacet nextFacetToMerge(final double eps,
		final Horizon frontFacet, final Vertex vTop)
	{
		Iterator<TriangularFacet> it = frontFacet.getNeighbors().iterator();
		while (it.hasNext()) {
			TriangularFacet f = it.next();
			if (f.distanceToPlane(vTop) > eps) {
				// if frontFacet contains all vertices of f it either is
				// connected
				// with two edges or one edge
				if (frontFacet.containsAll(f.getVertices())) {
					Vertex v0 = f.getVertex(0);
					Vertex v1 = f.getVertex(1);
					Vertex v2 = f.getVertex(2);
					int numEdges = 0;
					if (frontFacet.hasEdge(v0, v2)) {
						numEdges++;
					}
					if (frontFacet.hasEdge(v2, v1)) {
						numEdges++;
					}
					if (frontFacet.hasEdge(v1, v0)) {
						numEdges++;
					}
					if (numEdges == 1) {
						// If a facet is only connected to the frontFacet with
						// one edge but all three vertices are part of
						// frontFacet another facet with two edges connected to
						// the frontFacet is available.
						// After all facets with two connected edges are merged
						// this facet will be connected with two edges as well
						// and will be merged.
						continue;
					}
				}
				// f is connected with one edge and the third vertex of f is
				// not part of frontFacet.
				return f;
			}
		}
		return null;
	}

	/**
	 * Assigns all points which are not part of the convex hull to a facet. A
	 * point is assigned to a facet if the point is in front of this facet. Every
	 * point is assigned to only one facet. If a facet has a point in front the
	 * facet is added to {@code facetsWithPointInFront}. After this call
	 * {@code vertices} is empty. Points which are behind all facets are removed
	 * because they are on the inside of the convex hull.
	 * 
	 * @param newFacets which could have a point in front
	 * @param facetsWithPointInFront
	 */
	private void assignPointsToFacets(final double eps,
		final Set<Vertex> vertices, final List<TriangularFacet> newFacets,
		final List<TriangularFacet> facets,
		final List<TriangularFacet> facetsWithPointInFront)
	{
		Iterator<Vertex> vertexIt = vertices.iterator();
		while (vertexIt.hasNext()) {
			Vertex v = vertexIt.next();

			Iterator<TriangularFacet> facetIt = newFacets.iterator();
			TriangularFacet maxFacet = null;
			double maxdis = eps;

			while (facetIt.hasNext()) {
				TriangularFacet f = facetIt.next();
				double distanceToPlane = f.distanceToPlane(v);
				// point is assigned to the facet with maximum distance
				if (distanceToPlane > maxdis) {
					maxdis = distanceToPlane;
					maxFacet = f;
				}
			}

			// If maxFacet == null this vertex is behind all facets and
			// therefore on the inside of the convex hull.
			if (maxFacet != null) {
				maxFacet.setVertexInFront(v, maxdis);
				if (!facetsWithPointInFront.contains(maxFacet)) {
					facetsWithPointInFront.add(maxFacet);
				}
			}
		}

		facets.addAll(newFacets);

		// All vertices are reassigned or are inside of the convex hull.
		vertices.clear();
	}

	/**
	 * Computes an initial simplex of four facets. The simplex consists of the
	 * four points v0-v3. v0 and v1 have the largest possible distance in one
	 * dimension. v2 is the point with the largest distance to v0----v1. v3 is the
	 * point with the largest distance to the plane described by v0, v1, v2.
	 */
	private double createSimplex(final Set<Vertex> vertices,
		final List<TriangularFacet> facets,
		final List<TriangularFacet> facetsWithPointInFront)
	{

		final Pair<Double, Vertex[]> minMax = computeMinMax(vertices);
		final double eps = minMax.getA();

		int i = getMaxDistPointIndex(minMax.getB());

		Vertex v0 = minMax.getB()[i];
		Vertex v1 = minMax.getB()[i + 3];

		vertices.remove(v0);
		vertices.remove(v1);

		Vertex v2 = getV2(eps, vertices, v0, v1);

		vertices.remove(v2);

		Vertex v3 = getV3(eps, vertices, v0, v1, v2);

		vertices.remove(v3);

		TriangularFacet f0 = new TriangularFacet(v0, v1, v2);
		if (f0.distanceToPlane(v3) > eps) {
			// change triangle orientation to counter clockwise
			Vertex tmp = v1;
			v1 = v2;
			v2 = tmp;
			f0 = new TriangularFacet(v0, v1, v2);
		}
		// v3 is behind f0
		assert f0.distanceToPlane(v3) < eps;

		TriangularFacet f1 = new TriangularFacet(v1, v0, v3);

		TriangularFacet f2 = new TriangularFacet(v2, v1, v3);

		TriangularFacet f3 = new TriangularFacet(v0, v2, v3);

		f0.setNeighbor(0, f3);
		f0.setNeighbor(1, f1);
		f0.setNeighbor(2, f2);

		f1.setNeighbor(0, f2);
		f1.setNeighbor(1, f0);
		f1.setNeighbor(2, f3);

		f2.setNeighbor(0, f3);
		f2.setNeighbor(1, f0);
		f2.setNeighbor(2, f1);

		f3.setNeighbor(0, f1);
		f3.setNeighbor(1, f0);
		f3.setNeighbor(2, f2);

		assert f0.distanceToPlane(v3) < eps;
		assert f1.distanceToPlane(v2) < eps;
		assert f2.distanceToPlane(v0) < eps;
		assert f3.distanceToPlane(v1) < eps;

		List<TriangularFacet> newFacets = new ArrayList<>();
		newFacets.add(f0);
		newFacets.add(f1);
		newFacets.add(f2);
		newFacets.add(f3);
		assignPointsToFacets(eps, vertices, newFacets, facets,
			facetsWithPointInFront);

		return eps;
	}

	/**
	 * Finds the point with the largest distance to the plane described by v0, v1,
	 * v2.
	 * 
	 * @param v0 Vertex of the plane.
	 * @param v1 Vertex of the plane.
	 * @param v2 Vertex of the plane.
	 * @return Vertex with the largest distance.
	 */
	private Vertex getV3(final double eps, final Set<Vertex> vertices,
		final Vertex v0, final Vertex v1, final Vertex v2)
	{
		double distPlanePoint = eps;
		Vertex v3 = null;
		Vector3D d0 = v1.subtract(v0);
		Vector3D d1 = v2.subtract(v0);
		Vector3D normal = d0.crossProduct(d1).normalize();
		for (final Vertex v : vertices) {
			double d = Math.abs(normal.dotProduct(v.subtract(v0)));
			if (d > distPlanePoint) {
				distPlanePoint = d;
				v3 = v;
			}
		}
		return v3;
	}

	/**
	 * Finds the vertex with the largest distance to the line described by v0, v1.
	 * 
	 * @param v0 Vertex of the line.
	 * @param v1 Vertex of the line.
	 * @return Vertex with the largest distance.
	 */
	private Vertex getV2(final double eps, final Set<Vertex> vertices,
		final Vertex v0, final Vertex v1)
	{
		Iterator<Vertex> it = vertices.iterator();

		// v0 -------------------------------------v1
		// |
		// | d
		// |
		// * v
		//
		// d = |(v - v0) x (v - v1)| / |(v1 - v0)|
		// We can omit the common denominator because it does not change over
		// all computations.
		double distLinePoint = eps;
		Vertex v2 = null;
		while (it.hasNext()) {
			Vertex v = it.next();
			Vector3D d0 = v.subtract(v1);
			Vector3D d1 = v.subtract(v0);

			double lengthSq = d0.crossProduct(d1).getNormSq();
			if (lengthSq > distLinePoint) {
				distLinePoint = lengthSq;
				v2 = v;
			}
		}
		return v2;
	}

	/**
	 * Computes the index of the dimension containing the points with the largest
	 * distance.
	 * 
	 * @param minMax Vertices with the min and max coordinates of each dimension.
	 * @return index of the dimension with the largest distance between two
	 *         points.
	 */
	private int getMaxDistPointIndex(final Vertex[] minMax) {
		double[] diff = new double[] { minMax[3].getX() - minMax[0].getX(),
			minMax[4].getY() - minMax[1].getY(), minMax[5].getZ() - minMax[2]
				.getZ() };

		double max = 0;
		int imax = 0;
		for (int i = 0; i < diff.length; i++) {
			if (diff[i] > max) {
				max = diff[i];
				imax = i;
			}
		}
		return imax;
	}

	/**
	 * Finds for each dimension the min and max vertex.
	 * 
	 * @return min and max vertices of each dimension
	 */
	private Pair<Double, Vertex[]> computeMinMax(final Set<Vertex> vertices) {
		Vertex[] minMax = new Vertex[6];
		double maxX, maxY, maxZ;
		double minX, minY, minZ;
		Iterator<Vertex> it = vertices.iterator();

		Vertex initPoint = it.next();
		for (int i = 0; i < minMax.length; i++) {
			minMax[i] = initPoint;
		}
		minX = maxX = initPoint.getX();
		minY = maxY = initPoint.getY();
		minZ = maxZ = initPoint.getZ();

		while (it.hasNext()) {
			Vertex v = it.next();
			if (v.getX() > maxX) {
				maxX = v.getX();
				minMax[3] = v;
			}
			else if (v.getX() < minX) {
				minX = v.getX();
				minMax[0] = v;
			}
			if (v.getY() > maxY) {
				maxY = v.getY();
				minMax[4] = v;
			}
			else if (v.getY() < minY) {
				minY = v.getY();
				minMax[2] = v;
			}
			if (v.getZ() > maxZ) {
				maxZ = v.getZ();
				minMax[5] = v;
			}
			else if (v.getZ() < minZ) {
				minZ = v.getZ();
				minMax[3] = v;
			}
		}

		// This epsilon formula comes from John Lloyd's quickhull
		// implementation http://www.cs.ubc.ca/~lloyd/java/quickhull3d.html
		final double eps = 3 * DOUBLE_PREC * (Math.max(Math.abs(maxX), Math.abs(
			minX)) + Math.max(Math.abs(maxY), Math.abs(minY)) + Math.max(Math.abs(
				maxZ), Math.abs(minZ)));

		return new ValuePair<>(eps, minMax);
	}

}
