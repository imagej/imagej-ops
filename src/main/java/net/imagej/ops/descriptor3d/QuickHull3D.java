package net.imagej.ops.descriptor3d;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D.ConvexHull3D;

/**
 * This quickhull implementation is based on the paper
 * "The Quickhull Algorithm for Convex Hulls" by Barber, Dobkin and Huhdanpaa
 * (http://dpd.cs.princeton.edu/Papers/BarberDobkinHuhdanpaa.pdf).
 * 
 * The computation of the initial simplex is inspired by John Lloyd's quickhull
 * implementation (http://www.cs.ubc.ca/~lloyd/java/quickhull3d.html).
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
@Plugin(type = Op.class, name = ConvexHull3D.NAME)
public class QuickHull3D extends AbstractFunctionOp<HashSet<Vertex>, DefaultFacets> implements ConvexHull3D {

	/**
	 * Vertices which are not part of the convex hull.
	 */
	private Set<Vertex> m_vertices;

	/**
	 * Created facets.
	 */
	private List<TriangularFacet> m_facets;

	/**
	 * Facets with points in front.
	 */
	private List<TriangularFacet> m_facetsWithPointInFront;

	/**
	 * Minimum distance between a point and a facet.
	 */
	private double m_epsilon;

	/**
	 * Precision of a double.
	 */
	private final double DOUBLE_PREC = 2.2204460492503131e-16;

	@Override
	public DefaultFacets compute(HashSet<Vertex> input) {
		DefaultFacets output = new DefaultFacets();
		m_vertices = new HashSet<Vertex>(input);
		m_facets = new ArrayList<TriangularFacet>();
		m_facetsWithPointInFront = new ArrayList<TriangularFacet>();
		computeHull();
		for (TriangularFacet f : m_facets) {
			output.addFace(f);
		}
		output.setEpsilon(m_epsilon);
		return output;
	}

	/**
	 * Compute the convex hull.
	 */
	private void computeHull() {
		createSimplex();
		while (!m_facetsWithPointInFront.isEmpty()) {
			TriangularFacet next = m_facetsWithPointInFront.remove(0);
			replaceFacet(next);
		}
	}

	/**
	 * Replaces a facet with at least three new facets.
	 * 
	 * @param facet
	 *            the facet to replace. At least one point must be in front of
	 *            next.
	 */
	private void replaceFacet(TriangularFacet facet) {
		Vertex v = facet.getMaximumDistanceVertex();
		Horizon horizon = computeHorizon(facet, v);
		List<TriangularFacet> newFaces = createFacets(horizon, v);
		assignPointsToFacets(newFaces);
	}

	/**
	 * Adds for each edge of the horizon and vTop a new facet.
	 * 
	 * @param horizon
	 *            facet of all facets seen from point vTop
	 * @param vTop
	 *            point which is added to the convex hull
	 * @return new created facets
	 */
	private List<TriangularFacet> createFacets(Horizon horizon, Vertex vTop) {
		List<TriangularFacet> newFacets = new ArrayList<TriangularFacet>();
		Vertex vLeft, vRight;

		// triangles 1 to n
		for (int i = 1; i < horizon.size(); i++) {
			vLeft = horizon.getVertex(i - 1);
			vRight = horizon.getVertex(i);
			
			TriangularFacet f = new TriangularFacet(vRight, vTop, vLeft);

			setNeighborZero(f, (TriangularFacet)horizon.getNeighbor(i));

			newFacets.add(f);
		}

		// triangle 0, this triangle connects the n-th triangle with
		// triangle number 1
		vRight = horizon.getVertex(0);
		vLeft = horizon.getLastVertex();
		
		TriangularFacet f = new TriangularFacet(vRight, vTop, vLeft);

		setNeighborZero(f, (TriangularFacet)horizon.getNeighbor(0));

		newFacets.add(f);

		// set neighbors 1 and 2 of each triangle
		// triangle 0 has triangle n and 1 as neighbors.
		connectTriangles(newFacets);
		return newFacets;
	}

	/**
	 * Sets neighbors for each new triangle. The triangles build a cone.
	 * 
	 * @param newFacets
	 *            the triangles
	 */
	private void connectTriangles(List<TriangularFacet> newFacets) {
		int lastFacetIndex = newFacets.size() - 1;
		for (int i = 1; i < lastFacetIndex; i++) {
			newFacets.get(i).setNeighbor(1, newFacets.get(i + 1));
			newFacets.get(i).setNeighbor(2, newFacets.get(i - 1));
		}
		newFacets.get(0).setNeighbor(1, newFacets.get(1));
		newFacets.get(0).setNeighbor(2, newFacets.get(lastFacetIndex));
		newFacets.get(lastFacetIndex).setNeighbor(1, newFacets.get(0));
		newFacets.get(lastFacetIndex).setNeighbor(2,
				newFacets.get(newFacets.size() - 2));
	}

	/**
	 * Sets the first neighbor of a new created triangle.
	 * 
	 * @param f
	 *            the new facet.
	 * @param n
	 *            the neighbor facet.
	 */
	private void setNeighborZero(TriangularFacet f, TriangularFacet n) {
		int vertexIndex = n.indexOfVertex(f.getVertex(2));
		n.replaceNeighbor(vertexIndex, f);

		f.setNeighbor(0, n);
	}

	/**
	 * Computes the horizon of vTop. The horizon is the merged facet of all
	 * facets which are in front of the point vTop.
	 * 
	 * @param frontFacet
	 *            a face which is in front of vTop
	 * @param vTop
	 *            a point outside of the convex hull
	 * @return facet containing all facets which are in front of vTop
	 */
	@SuppressWarnings("unchecked")
	private Horizon computeHorizon(TriangularFacet frontFacet, Vertex vTop) {
		// Points which are in front have to be reassigned after all new facets
		// are constructed.
		m_vertices.addAll(frontFacet.getVerticesInFront());

		// frontFacet is not a result facet. Remove it from result list.
		m_facets.remove(frontFacet);

		Horizon h = new Horizon(frontFacet);
		TriangularFacet merge = nextFacetToMerge(h, vTop);
		while (merge != null) {
			// This points have to be reassigned as well.
			m_vertices.addAll(merge.getVerticesInFront());
			// This face has some points in front and therefore is not a result
			// face.
			m_facets.remove(merge);
			// After this step this facet is merged with another facet.
			m_facetsWithPointInFront.remove(merge);

			if (h.containsAll((List<Vertex>)merge.getVertices())) {
				updateNeighbors(frontFacet, merge);
				h.complexMerge(merge);
			} else {
				updateNeighbors(frontFacet, merge);
				h.simpleMerge(merge);
			}
			merge = nextFacetToMerge(h, vTop);
		}

		return h;
	}

	/**
	 * After the merge step the facet merge is part of frontFacet. Therefore the
	 * neighbors of merge must point to frontFacet and not to merge.
	 * 
	 * @param frontFacet
	 *            the facet to which merge will be added.
	 * @param merge
	 *            the facet which will be merged with frontFacet.
	 */
	private void updateNeighbors(TriangularFacet frontFacet, TriangularFacet merge) {
		for (AbstractPolygon f : merge.getNeighbors()) {
			if (!f.equals(frontFacet)) {
				f.replaceNeighbor(f.indexOfNeighbor(merge), frontFacet);
			}
		}
	}

	/**
	 * Returns a facet which is in front of vTop and neighbor of front.
	 * 
	 * @param frontFacet
	 *            facet in front of vTop
	 * @param vTop
	 *            point which is added to the convex hull
	 * @return neighboring facet of front or null if no facet is in front
	 */
	@SuppressWarnings("unchecked")
	private TriangularFacet nextFacetToMerge(Horizon frontFacet,
			Vertex vTop) {
		Iterator<AbstractPolygon> it = frontFacet.getNeighbors().iterator();
		while (it.hasNext()) {
			TriangularFacet f = (TriangularFacet)it.next();
			if (f.distanceToPlane(vTop) > m_epsilon) {
				// if frontFacet contains all vertices of f it either is
				// connected
				// with two edges or one edge
				if (frontFacet.containsAll((List<Vertex>)f.getVertices())) {
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
	 * point is assigned to a facet if the point is in front of this facet.
	 * Every point is assigned to only one facet. If a facet has a point in
	 * front the facet is added to {@link QuickHull3D#m_facetsWithPointInFront}.
	 * After this call {@link QuickHull3D#m_vertices} is empty. Points which are
	 * behind all facets are removed because they are on the inside of the
	 * convex hull.
	 * 
	 * @param facets
	 *            which could have a point in front
	 */
	private void assignPointsToFacets(List<TriangularFacet> facets) {
		Iterator<Vertex> vertexIt = m_vertices.iterator();
		while (vertexIt.hasNext()) {
			Vertex v = vertexIt.next();

			Iterator<TriangularFacet> facetIt = facets.iterator();
			TriangularFacet maxFacet = null;
			double maxdis = m_epsilon;

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
				if (!m_facetsWithPointInFront.contains(maxFacet)) {
					m_facetsWithPointInFront.add(maxFacet);
				}
			}
		}
		
		m_facets.addAll(facets);
		
		// All vertices are reassigned or are inside of the convex hull.
		m_vertices.clear();
	}

	/**
	 * Computes an initial simplex of four facets. The simplex consists of the
	 * four points v0-v3. v0 and v1 have the largest possible distance in one
	 * dimension. v2 is the point with the largest distance to v0----v1. v3 is
	 * the point with the largest distance to the plane described by v0, v1, v2.
	 */
	private void createSimplex() {
		Vertex[] minMax = computeMinMax();
		int i = getMaxDistPointIndex(minMax);

		Vertex v0 = minMax[i];
		Vertex v1 = minMax[i + 3];

		m_vertices.remove(v0);
		m_vertices.remove(v1);

		Vertex v2 = getV2(v0, v1);

		m_vertices.remove(v2);

		Vertex v3 = getV3(v0, v1, v2);

		m_vertices.remove(v3);
		
		TriangularFacet f0 = new TriangularFacet(v0, v1, v2);
		if (f0.distanceToPlane(v3) > m_epsilon) {
			// change triangle orientation to counter clockwise
			Vertex tmp = v1;
			v1 = v2;
			v2 = tmp;
			f0 = new TriangularFacet(v0, v1, v2);
		}
		// v3 is behind f0
		assert f0.distanceToPlane(v3) < m_epsilon;

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

		assert f0.distanceToPlane(v3) < m_epsilon;
		assert f1.distanceToPlane(v2) < m_epsilon;
		assert f2.distanceToPlane(v0) < m_epsilon;
		assert f3.distanceToPlane(v1) < m_epsilon;

		List<TriangularFacet> newFacets = new ArrayList<TriangularFacet>();
		newFacets.add(f0);
		newFacets.add(f1);
		newFacets.add(f2);
		newFacets.add(f3);
		assignPointsToFacets(newFacets);
	}

	/**
	 * Finds the point with the largest distance to the plane described by v0,
	 * v1, v2.
	 * 
	 * @param v0
	 *            Vertex of the plane.
	 * @param v1
	 *            Vertex of the plane.
	 * @param v2
	 *            Vertex of the plane.
	 * @return Vertex with the largest distance.
	 */
	private Vertex getV3(Vertex v0, Vertex v1, Vertex v2) {
		double distPlanePoint = m_epsilon;
		Vertex v3 = null;
		Iterator<Vertex> it = m_vertices.iterator();
		Vector3D d0 = v1.subtract(v0);
		Vector3D d1 = v2.subtract(v0);
		Vector3D normal = d0.crossProduct(d1).normalize();
		while (it.hasNext()) {
			Vertex v = it.next();
			double d = Math.abs(normal.dotProduct(v.subtract(v0)));
			if (d > distPlanePoint) {
				distPlanePoint = d;
				v3 = v;
			}
		}
		return v3;
	}

	/**
	 * Finds the vertex with the largest distance to the line described by v0,
	 * v1.
	 * 
	 * @param v0
	 *            Vertex of the line.
	 * @param v1
	 *            Vertex of the line.
	 * @return Vertex with the largest distance.
	 */
	private Vertex getV2(Vertex v0, Vertex v1) {
		Iterator<Vertex> it = m_vertices.iterator();

		// v0 -------------------------------------v1
		// |
		// | d
		// |
		// * v
		//
		// d = |(v - v0) x (v - v1)| / |(v1 - v0)|
		// We can omit the common denominator because it does not change over
		// all computations.
		double distLinePoint = m_epsilon;
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
	 * Computes the index of the dimension containing the points with the
	 * largest distance.
	 * 
	 * @param minMax
	 *            Vertices with the min and max coordinates of each dimension.
	 * @return index of the dimension with the largest distance between two
	 *         points.
	 */
	private int getMaxDistPointIndex(Vertex[] minMax) {
		double[] diff = new double[]{minMax[3].getX() - minMax[0].getX(),
				minMax[4].getY() - minMax[1].getY(),
				minMax[5].getZ() - minMax[2].getZ()};

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
	private Vertex[] computeMinMax() {
		Vertex[] minMax = new Vertex[6];
		double maxX, maxY, maxZ;
		double minX, minY, minZ;
		Iterator<Vertex> it = m_vertices.iterator();

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
			} else if (v.getX() < minX) {
				minX = v.getX();
				minMax[0] = v;
			}
			if (v.getY() > maxY) {
				maxY = v.getY();
				minMax[4] = v;
			} else if (v.getY() < minY) {
				minY = v.getY();
				minMax[2] = v;
			}
			if (v.getZ() > maxZ) {
				maxZ = v.getZ();
				minMax[5] = v;
			} else if (v.getZ() < minZ) {
				minZ = v.getZ();
				minMax[3] = v;
			}
		}

		// This epsilon formula comes from John Lloyd's quickhull
		// implementation http://www.cs.ubc.ca/~lloyd/java/quickhull3d.html
		m_epsilon = 3 * DOUBLE_PREC
				* (Math.max(Math.abs(maxX), Math.abs(minX))
						+ Math.max(Math.abs(maxY), Math.abs(minY))
						+ Math.max(Math.abs(maxZ), Math.abs(minZ)));

		return minMax;
	}
}
