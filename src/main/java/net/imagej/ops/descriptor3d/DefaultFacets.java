package net.imagej.ops.descriptor3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * This is the default implementation of {@link Facets}. 
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class DefaultFacets implements Facets, Iterable<TriangularFacet> {

	/**
	 * All {@link TriangularFacet}. 
	 */
	private ArrayList<TriangularFacet> m_facets;
	
	/**
	 * All unique vertices of all {@link DefaultFacets#m_facets}.
	 */
	private HashSet<Vertex> m_points;
	
	/**
	 * The sum of the area of all {@link DefaultFacets#m_facets}.
	 */
	private double m_area;
	
	/**
	 * The centroid of all {@link DefaultFacets#m_facets}.
	 */
	private Vertex m_centroid;

	/**
	 * The epsilon which was used to compute all {@link DefaultFacets#m_facets}.
	 */
	private double m_epsilon;

	/**
	 * A new empty facet container. 
	 */
	public DefaultFacets() {
		m_facets = new ArrayList<TriangularFacet>();
		m_points = new HashSet<Vertex>();
		m_area = 0;
	}

	/**
	 * Get all facets.
	 * @return the facets
	 */
	public ArrayList<TriangularFacet> getFacets() {
		return m_facets;
	}

	/**
	 * Set the facets. 
	 * @param facets to set
	 */
	public void setFaces(ArrayList<TriangularFacet> facets) {
		this.m_facets = facets;
	}
	
	/**
	 * Add a new facet. 
	 * @param f the facet to add
	 */
	public void addFace(TriangularFacet f) {
		m_facets.add(f);
		m_area += f.getArea();

		m_points.addAll((List<Vertex>) f.getVertices());
	}
	
	/**
	 * Get all unique points. 
	 * @return the unique points
	 */
	public HashSet<Vertex> getPoints() {
		return m_points;
	}

	/**
	 * Get the area of all facets combined. 
	 * @return the total area
	 */
	public double getArea() {
		return m_area;
	}
	
	/**
	 * Get the centroid of all facets.
	 * @return the centroid
	 */
	public Vertex getCentroid() {
		if (m_centroid == null) {
			Iterator<Vertex> it = m_points.iterator();
			double x,y,z = y = x = 0;
			while (it.hasNext()) {
				Vertex next = it.next();
				x += next.getX();
				y += next.getY();
				z += next.getZ();
			}
			
			x /= m_points.size();
			y /= m_points.size();
			z /= m_points.size();
			m_centroid = new Vertex(x, y, z);
		}
		return m_centroid;
	}

	@Override
	public Iterator<TriangularFacet> iterator() {
		return m_facets.iterator();
	}

	/**
	 * Set the epsilon which was used in the facet computation.
	 * @param epsilon the epsilon
	 */
	public void setEpsilon(double epsilon) {
		m_epsilon = epsilon;
	}
	
	/**
	 * Get the used epsilon.
	 * @return the epsilon
	 */
	public double getEpsilon() {
		return m_epsilon;
	}

	/**
	 * Set the points.
	 * Note: No facets are constructed.
	 * @param points to set
	 */
	public void setPoints(HashSet<Vertex> points) {
		this.m_points = points;
	}
}
