package net.imagej.ops.descriptor3d;

import java.util.HashSet;
import java.util.List;

import net.imglib2.RealLocalizable;

/**
 * A Horizon is the result of n neighboring {@link TriangularFacet} which are merged.
 * The horizon is a polygon of all outer edges/vertices of the merged
 * {@link TriangularFacet}. 
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
public class Horizon {
	
	private List<Vertex> m_vertices;
	
	private List<AbstractPolygon> m_neighbors;
	
	/**
	 * Create a new {@link Horizon} from a {@link TriangularFacet}
	 * @param f the facet
	 */
	@SuppressWarnings("unchecked")
	public Horizon (TriangularFacet f) {
		m_vertices = (List<Vertex>) f.getVertices();
		m_neighbors = f.getNeighbors();
	}

	/**
	 * Merges another facet to this facet.
	 * The facet has to be connected to this facet by only one edge
	 * and only the tail and head vertex of this edge are part of this 
	 * facet.
	 * Note: The neighbors of f pointing to f have to be updated manually. 
	 * @param f the facet to merge into this facet.
	 */
	public void simpleMerge(TriangularFacet f) {
		int neighborIndex = m_neighbors.indexOf(f);
		int newVertex = -1;
		for (int i = 0; i < f.getVertices().size(); i++) {
			if (m_vertices.indexOf(f.getVertex(i)) == -1) {
				newVertex = i;
				break;
			}
		}
		m_vertices.add(neighborIndex, f.getVertex(newVertex));
		m_neighbors.remove(neighborIndex);
		m_neighbors.add(neighborIndex, f.getNeighbor(newVertex));
		neighborIndex = (neighborIndex + 1) % (m_neighbors.size()+1);
		newVertex = (newVertex + 1) % 3;
		m_neighbors.add(neighborIndex, f.getNeighbor(newVertex));
	}

	/**
	 * Merges another facet to this facet.
	 * The facet has to be connected to this facet by only two consecutive edges
	 * and only the tail and head vertices of these edges are part of this 
	 * facet.
	 * Note: The neighbors of f pointing to f have to be updated manually.
	 * @param f the facet to merge into this facet.
	 */
	public void complexMerge(TriangularFacet f) {
		Vertex v0 = f.getVertex(0);
		Vertex v1 = f.getVertex(1);
		Vertex v2 = f.getVertex(2);
		if (hasEdge(v0, v2)) {
			if (hasEdge(v1, v0)) {
				mergeTwoAdjacentEdges(f, v0, 2);
			} else if (hasEdge(v2, v1)) {
				mergeTwoAdjacentEdges(f, v2, 1);
			} 
		} else if (hasEdge(v2, v1)) {
			if (hasEdge(v0, v2)) {
				mergeTwoAdjacentEdges(f, v2, 1);
			} else if (hasEdge(v1, v0)) {
				mergeTwoAdjacentEdges(f, v1, 0);
			}
		} else if (hasEdge(v1, v0)) {
			if (hasEdge(v2, v1)) {
				mergeTwoAdjacentEdges(f, v1, 0);
			} else if (hasEdge(v0, v2)) {
				mergeTwoAdjacentEdges(f, v0, 2);
			} 
		}
	}

	/**
	 * Merge a triangle with two adjacent edges into the horizon. 
	 * @param f the triangle to merge
	 * @param v0 the vertex of the triangle which lies between the two edges
	 * @param neighborIndex of the new outer neighbor
	 */
	private void mergeTwoAdjacentEdges(TriangularFacet f, Vertex v0, int neighborIndex) {
		int i = m_vertices.indexOf(v0) ;
		m_vertices.remove(i);
		m_neighbors.remove(i);
		i = i  % m_neighbors.size();
		m_neighbors.remove(i);
		m_neighbors.add(i, f.getNeighbor(neighborIndex));
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

	public Vertex getVertex(int i) {
		return m_vertices.get(i);
	}

	public int size() {
		return m_vertices.size();
	}

	public AbstractPolygon getNeighbor(int i) {
		return m_neighbors.get(i);
	}

	/**
	 * Get the last vertex.
	 * @return the last vertex
	 */
	public Vertex getLastVertex() {
		return m_vertices.get(size() - 1);
	}

	public boolean containsAll(List<Vertex> vertices) {
		return m_vertices.containsAll(vertices);
	}

	public List<AbstractPolygon> getNeighbors() {
		return m_neighbors;
	}
}
