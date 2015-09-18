package net.imagej.ops.descriptor3d;

import java.util.List;

/**
 * An {@link AbstractPolygon} consists of vertices and neighbors.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 *
 */
public abstract class AbstractPolygon {
	
	/**
	 * The vertices of this facet in counter clock wise orientation.
	 */
	protected List<Vertex> m_vertices;
	
	/**
	 * The neighboring facets of this facet. 
	 * Neighbor 0 is the neighbor which is adjacent to this
	 * facet at the edge from {@link TriangularFacet#getLastVertex()}
	 * to {@link DefaultFacet#getVertex(0)}. 
	 */
	protected List<AbstractPolygon> m_neighbors;
		
	/**
	 * The list of vertices.
	 * @return all vertices
	 */
	public List<Vertex> getVertices() {
		return m_vertices;
	}
	
	/**
	 * Returns the index of the first occurrence of vertex.
	 * @param vertex the vertex 
	 * @return index of vertex or -1 if this vertex is not contained
	 */
	public int indexOfVertex(Vertex vertex) {
		return m_vertices.indexOf(vertex);
	}

	/**
	 * Get the vertex at index i.
	 * @param i the position
	 * @return the vertex
	 */
	public Vertex getVertex(int i) {
		return m_vertices.get(i);
	}
	
	/**
	 * Get the number of vertices.
	 * @return number of vertices
	 */
	public int size() {
		return m_vertices.size();
	}

	/**
	 * Get the last vertex.
	 * @return the last vertex
	 */
	public Vertex getLastVertex() {
		return m_vertices.get(m_vertices.size() - 1);
	}
	
	/**
	 * Returns true if all vertices are part of this polygon
	 * @param vertices to check
	 * @return true if all vertices are contained
	 */
	public boolean containsAll(List<Vertex> vertices) {
		return m_vertices.containsAll(vertices);
	}
	
	/**
	 * Returns true if this facet has the edge from tail to head.
	 * @param tail vertex of the edge
	 * @param head vertex of the edge
	 * @return has edge tail to head
	 */
	public boolean hasEdge(Vertex tail, Vertex head) {
		int start = m_vertices.indexOf(tail);
		int end = m_vertices.indexOf(head);
		if (start == -1 || end == -1) {
			return false;
		}
		return (start + 1) % m_vertices.size() == end;
	}
	
	/**
	 * Sets the n-th neighbor of this facet.
	 * @param position of the neighbor
	 * @param n the neighbor
	 */
	public void setNeighbor(int position, TriangularFacet n) {
		m_neighbors.add(position, n);
	}

	/**
	 * Get the neighbor at position.
	 * @param position the position
	 * @return the neighbor
	 */
	public AbstractPolygon getNeighbor(int position) {
		return m_neighbors.get(position);
	}
	
	/**
	 * Get all neighbors.
	 * @return all neighbors
	 */
	public List<AbstractPolygon> getNeighbors() {
		return m_neighbors;
	}

	/**
	 * Replaces a neighbor.
	 * @param i index of the neighbor to replace
	 * @param f the new neighbor
	 */
	public void replaceNeighbor(int i, AbstractPolygon f) {
		m_neighbors.remove(i);
		m_neighbors.add(i, f);
	}
	
	/**
	 * Get index of a neighbor.
	 * @param facet the neighboring facet
	 * @return the index or -1 if facet is not a neighbor
	 */
	public int indexOfNeighbor(TriangularFacet facet) {
		return m_neighbors.indexOf(facet);
	}
}