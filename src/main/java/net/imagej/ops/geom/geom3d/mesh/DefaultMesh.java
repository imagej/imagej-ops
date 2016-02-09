/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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

package net.imagej.ops.geom.geom3d.mesh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.imglib2.RealLocalizable;

/**
 * This is the default implementation of {@link Mesh}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
public class DefaultMesh implements Mesh, Iterable<Facet> {

	/**
	 * All {@link TriangularFacet}.
	 */
	private List<Facet> facets;

	/**
	 * All unique vertices of all {@link DefaultMesh#facets}.
	 */
	private Set<RealLocalizable> vertices;

	/**
	 * The sum of the area of all {@link DefaultMesh#facets}.
	 */
	private double area;

	/**
	 * The epsilon which was used to compute all {@link DefaultMesh#facets}.
	 */
	private double epsilon;

	/**
	 * A new empty facet container.
	 */
	public DefaultMesh() {
		facets = new ArrayList<>();
		vertices = new LinkedHashSet<>();
		area = 0;
	}

	/**
	 * A new empty facet container.
	 */
	public DefaultMesh(final Set<RealLocalizable> vertices) {
		facets = new ArrayList<>();
		area = 0;
		this.vertices = vertices;
	}

	/**
	 * Get all facets.
	 * 
	 * @return the facets
	 */
	@Override
	public List<Facet> getFacets() {
		return facets;
	}

	/**
	 * Set the facets.
	 * 
	 * @param facets to set
	 */
	public void setFaces(final List<Facet> facets) {
		this.facets = facets;
	}

	/**
	 * Add a new facet.
	 * 
	 * @param f the facet to add
	 */
	public void addFace(final TriangularFacet f) {
		facets.add(f);
		area += f.getArea();

		vertices.addAll(f.getVertices());
	}

	/**
	 * Get the area of all facets combined.
	 * 
	 * @return the total area
	 */
	@Override
	public double getSurfaceArea() {
		return area;
	}

	@Override
	public Iterator<Facet> iterator() {
		return facets.iterator();
	}

	/**
	 * Set the epsilon which was used in the facet computation.
	 * 
	 * @param epsilon the epsilon
	 */
	public void setEpsilon(final double epsilon) {
		this.epsilon = epsilon;
	}

	/**
	 * Get the used epsilon.
	 * 
	 * @return the epsilon
	 */
	public double getEpsilon() {
		return epsilon;
	}

	@Override
	public boolean triangularFacets() {
		return true;
	}

	@Override
	public Set<RealLocalizable> getVertices() {
		return vertices;
	}

}
