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
package net.imagej.ops.geometric3d;


/**
 * A Horizon is the result of n neighboring {@link TriangularFacet} which are merged.
 * The horizon is a polygon of all outer edges/vertices of the merged
 * {@link TriangularFacet}. 
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
public class Horizon extends UpdateablePointSet {
	
	/**
	 * Create a new {@link Horizon} from a {@link TriangularFacet}
	 * @param f the facet
	 */
	public Horizon (final TriangularFacet f) {
		vertices = f.getVertices();
		neighbors = f.getNeighbors();
	}

	/**
	 * Merges another facet to this facet.
	 * The facet has to be connected to this facet by only one edge
	 * and only the tail and head vertex of this edge are part of this 
	 * facet.
	 * Note: The neighbors of f pointing to f have to be updated manually. 
	 * @param f the facet to merge into this facet.
	 */
	public void simpleMerge(final TriangularFacet f) {
		int neighborIndex = neighbors.indexOf(f);
		int newVertex = -1;
		for (int i = 0; i < f.getVertices().size(); i++) {
			if (vertices.indexOf(f.getVertex(i)) == -1) {
				newVertex = i;
				break;
			}
		}
		vertices.add(neighborIndex, f.getVertex(newVertex));
		neighbors.remove(neighborIndex);
		neighbors.add(neighborIndex, f.getNeighbor(newVertex));
		neighborIndex = (neighborIndex + 1) % (neighbors.size()+1);
		newVertex = (newVertex + 1) % 3;
		neighbors.add(neighborIndex, f.getNeighbor(newVertex));
	}

	/**
	 * Merges another facet to this facet.
	 * The facet has to be connected to this facet by only two consecutive edges
	 * and only the tail and head vertices of these edges are part of this 
	 * facet.
	 * Note: The neighbors of f pointing to f have to be updated manually.
	 * @param f the facet to merge into this facet.
	 */
	public void complexMerge(final TriangularFacet f) {
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
	private void mergeTwoAdjacentEdges(final TriangularFacet f, final Vertex v0, final int neighborIndex) {
		int i = vertices.indexOf(v0) ;
		vertices.remove(i);
		neighbors.remove(i);
		i = i  % neighbors.size();
		neighbors.remove(i);
		neighbors.add(i, f.getNeighbor(neighborIndex));
	}
}
