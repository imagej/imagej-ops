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
public class TriangularFacet extends UpdateablePointSet implements Facet {

	/**
	 * The centroid of this facet.
	 */
	private Vector3D centroid = null;

	/**
	 * The normal of this facet.
	 */
	private Vector3D normal = null;

	/**
	 * The area of this facet.
	 */
	private double area = -1;


	/**
	 * If a facet has points in front, they are stored in this list.
	 * This list is used in {@link DefaultConvexHull3D}.
	 */
	private List<Vertex> verticesInFront;

	/**
	 * Creates a new empty facet.
	 */
	public TriangularFacet() {
		vertices = new ArrayList<Vertex>();
		verticesInFront = new ArrayList<Vertex>();
	}

	/**
	 * Creates a new facet of three vertices.
	 * @param v0 the first vertex
	 * @param v1 the second vertex
	 * @param v2 the third vertex
	 */
	public TriangularFacet(final Vertex v0, final Vertex v1, final Vertex v2) {
		vertices = new ArrayList<Vertex>();
		vertices.add(v0);
		vertices.add(v1);
		vertices.add(v2);
		verticesInFront = new ArrayList<Vertex>();
		neighbors = new ArrayList<UpdateablePointSet>();
	}
	
	/**
	 * Get the area of this facet.
	 * @return the area
	 */
	public double getArea() {
		if (area == -1) {
			computeArea();
		}
		return area;
	}

	/**
	 * Compute the area of this facet.
	 */
	private void computeArea() {
		Vector3D cross = vertices.get(0).subtract(vertices.get(1))
				.crossProduct(vertices.get(2).subtract(vertices.get(0)));
		area = cross.getNorm() * 0.5;
	}

	/**
	 * Get the centroid of this facet.
	 * @return the centroid
	 */
	public Vector3D getCentroid() {
		if (centroid == null) {
			computeCentroid();
		}
		return centroid;
	}

	/**
	 * Compute the centroid of this facet.
	 */
	private void computeCentroid() {
		centroid = Vector3D.ZERO;
		Iterator<Vertex> it = vertices.iterator();

		while (it.hasNext()) {
			centroid = centroid.add(it.next());
		}
		centroid = centroid.scalarMultiply(1 / (double) vertices.size());
	}

	/**
	 * Get the normal of this facet.
	 * @return the normal
	 */
	public Vector3D getNormal() {
		if (normal == null) {
			computeNormal();
		}
		return normal;
	}

	/**
	 * Compute the normal of this facet.
	 */
	private void computeNormal() {
		Vector3D v0 = vertices.get(0);
		Vector3D v1 = vertices.get(1);
		Vector3D v2 = vertices.get(2);
		normal = v1.subtract(v0).crossProduct(v2.subtract(v0)).normalize();
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
	public double distanceToPlane(final Vector3D p) {
		return getNormal().dotProduct(p) - getPlaneOffset();
	}

	/** 
	 * Adds a vertex to the points in front of this facet.
	 * @param v the vertex
	 * @param distanceToPlane of this vertex
	 */
	public void setVertexInFront(final Vertex v, final double distanceToPlane) {
		if (verticesInFront.isEmpty()) {
			v.setDistanceToFaceInFront(distanceToPlane);
			verticesInFront.add(v);
		} else {
			if (verticesInFront.get(0)
					.getDistanceToFaceInFront() < distanceToPlane) {
				v.setDistanceToFaceInFront(distanceToPlane);
				verticesInFront.add(0, v);
			} else {
				verticesInFront.add(v);
			}
		}
	}

	/**
	 * All points which are in front of this plane.
	 * @return points which are in front
	 */
	public List<Vertex> getVerticesInFront() {
		return verticesInFront;
	}
	
	/**
	 * The vertex which is in front and farthest apart of the plane
	 * @return vertex with maximum distance to the plane
	 */
	public Vertex getMaximumDistanceVertex() {
		return verticesInFront.remove(0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(area);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((centroid == null) ? 0 : centroid.hashCode());
		result = prime * result
				+ ((neighbors == null) ? 0 : neighbors.hashCode());
		result = prime * result
				+ ((normal == null) ? 0 : normal.hashCode());
		result = prime * result
				+ ((verticesInFront == null) ? 0 : verticesInFront.hashCode());
		result = prime * result
				+ ((vertices == null) ? 0 : vertices.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TriangularFacet other = (TriangularFacet) obj;
		if (Double.doubleToLongBits(area) != Double
				.doubleToLongBits(other.area))
			return false;
		if (centroid == null) {
			if (other.centroid != null)
				return false;
		} else if (!centroid.equals(other.centroid))
			return false;
		if (neighbors == null) {
			if (other.neighbors != null)
				return false;
		} else if (!neighbors.equals(other.neighbors))
			return false;
		if (normal == null) {
			if (other.normal != null)
				return false;
		} else if (!normal.equals(other.normal))
			return false;
		if (verticesInFront == null) {
			if (other.verticesInFront != null)
				return false;
		} else if (!verticesInFront.equals(other.verticesInFront))
			return false;
		if (vertices == null) {
			if (other.vertices != null)
				return false;
		} else if (!vertices.equals(other.vertices))
			return false;
		return true;
	}
}
