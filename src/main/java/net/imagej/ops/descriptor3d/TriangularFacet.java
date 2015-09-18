package net.imagej.ops.descriptor3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This is the triangle implementation of Facet Interface.
 * A facet consists of three vertices. The triangles orientation
 * is counter clock wise. 
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class TriangularFacet extends AbstractPolygon implements FacetInterface {

	/**
	 * The centroid of this facet.
	 */
	private Vector3D m_centroid = null;

	/**
	 * The normal of this facet.
	 */
	private Vector3D m_normal = null;

	/**
	 * The area of this facet.
	 */
	private double m_area = -1;


	/**
	 * If a facet has points in front, they are stored in this list.
	 * This list is used in {@link QuickHull3D}.
	 */
	private List<Vertex> m_verticesInFront;

	/**
	 * Creates a new empty facet.
	 */
	public TriangularFacet() {
		m_vertices = new ArrayList<Vertex>();
		m_verticesInFront = new ArrayList<Vertex>();
	}

	/**
	 * Creates a new facet of three vertices.
	 * @param v0 the first vertex
	 * @param v1 the second vertex
	 * @param v2 the third vertex
	 */
	public TriangularFacet(Vertex v0, Vertex v1, Vertex v2) {
		m_vertices = new ArrayList<Vertex>();
		m_vertices.add(v0);
		m_vertices.add(v1);
		m_vertices.add(v2);
		m_verticesInFront = new ArrayList<Vertex>();
		m_neighbors = new ArrayList<AbstractPolygon>();
	}
	
	/**
	 * Get the area of this facet.
	 * @return the area
	 */
	public double getArea() {
		if (m_area == -1) {
			computeArea();
		}
		return m_area;
	}

	/**
	 * Compute the area of this facet.
	 */
	private void computeArea() {
		Vector3D cross = m_vertices.get(0).subtract(m_vertices.get(1))
				.crossProduct(m_vertices.get(2).subtract(m_vertices.get(0)));
		m_area = cross.getNorm() * 0.5;
	}

	/**
	 * Get the centroid of this facet.
	 * @return the centroid
	 */
	public Vector3D getCentroid() {
		if (m_centroid == null) {
			computeCentroid();
		}
		return m_centroid;
	}

	/**
	 * Compute the centroid of this facet.
	 */
	private void computeCentroid() {
		m_centroid = Vector3D.ZERO;
		Iterator<Vertex> it = m_vertices.iterator();

		while (it.hasNext()) {
			m_centroid = m_centroid.add(it.next());
		}
		m_centroid = m_centroid.scalarMultiply(1 / (double) m_vertices.size());
	}

	/**
	 * Get the normal of this facet.
	 * @return the normal
	 */
	public Vector3D getNormal() {
		if (m_normal == null) {
			computeNormal();
		}
		return m_normal;
	}

	/**
	 * Compute the normal of this facet.
	 */
	private void computeNormal() {
		Vector3D v0 = m_vertices.get(0);
		Vector3D v1 = m_vertices.get(1);
		Vector3D v2 = m_vertices.get(2);
		m_normal = v1.subtract(v0).crossProduct(v2.subtract(v0)).normalize();
	}

	/**
	 * Computes the offset of this facet
	 * @return the offset
	 */
	public double getPlaneOffset() {
		return getNormal().dotProduct(getCentroid());
	}

	/**
	 * Computes the distance from a point to this facet
	 * @param p the point
	 * @return the distance
	 */
	public double distanceToPlane(Vector3D p) {
		return getNormal().dotProduct(p) - getPlaneOffset();
	}

	/** 
	 * Adds a vertex to the points in front of this facet.
	 * @param v the vertex
	 * @param distanceToPlane of this vertex
	 */
	public void setVertexInFront(Vertex v, double distanceToPlane) {
		if (m_verticesInFront.isEmpty()) {
			v.setDistanceToFaceInFront(distanceToPlane);
			m_verticesInFront.add(v);
		} else {
			if (m_verticesInFront.get(0)
					.getDistanceToFaceInFront() < distanceToPlane) {
				v.setDistanceToFaceInFront(distanceToPlane);
				m_verticesInFront.add(0, v);
			} else {
				m_verticesInFront.add(v);
			}
		}
	}

	/**
	 * All points which are in front of this plane.
	 * @return points which are in front
	 */
	public List<Vertex> getVerticesInFront() {
		return m_verticesInFront;
	}
	
	/**
	 * The vertex which is in front and farthest apart of the plane
	 * @return vertex with maximum distance to the plane
	 */
	public Vertex getMaximumDistanceVertex() {
		return m_verticesInFront.remove(0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(m_area);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((m_centroid == null) ? 0 : m_centroid.hashCode());
		result = prime * result
				+ ((m_neighbors == null) ? 0 : m_neighbors.hashCode());
		result = prime * result
				+ ((m_normal == null) ? 0 : m_normal.hashCode());
		result = prime * result
				+ ((m_verticesInFront == null) ? 0 : m_verticesInFront.hashCode());
		result = prime * result
				+ ((m_vertices == null) ? 0 : m_vertices.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TriangularFacet other = (TriangularFacet) obj;
		if (Double.doubleToLongBits(m_area) != Double
				.doubleToLongBits(other.m_area))
			return false;
		if (m_centroid == null) {
			if (other.m_centroid != null)
				return false;
		} else if (!m_centroid.equals(other.m_centroid))
			return false;
		if (m_neighbors == null) {
			if (other.m_neighbors != null)
				return false;
		} else if (!m_neighbors.equals(other.m_neighbors))
			return false;
		if (m_normal == null) {
			if (other.m_normal != null)
				return false;
		} else if (!m_normal.equals(other.m_normal))
			return false;
		if (m_verticesInFront == null) {
			if (other.m_verticesInFront != null)
				return false;
		} else if (!m_verticesInFront.equals(other.m_verticesInFront))
			return false;
		if (m_vertices == null) {
			if (other.m_vertices != null)
				return false;
		} else if (!m_vertices.equals(other.m_vertices))
			return false;
		return true;
	}
}
