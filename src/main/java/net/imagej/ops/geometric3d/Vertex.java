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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.imglib2.RealLocalizable;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Represents vertices of the hull, as well as the points from which it is
 * formed.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
public class Vertex extends Vector3D implements RealLocalizable {

	private double distanceToFaceInFront = 0;
	
	private List<TriangularFacet> facesInFront = new ArrayList<TriangularFacet>();
	
	public Vertex(final double x, final double y, final double z) {
		super(x, y, z);
	}

	public double getDistanceToFaceInFront() {
		return distanceToFaceInFront;
	}

	public void setDistanceToFaceInFront(final double m_distanceToFaceInFront) {
		this.distanceToFaceInFront = m_distanceToFaceInFront;
	}

	public void addFaceInFront(final TriangularFacet f) {
		facesInFront.add(f);
	}
	
	public void clearFacesInFront() {
		facesInFront.clear();
	}
	
	
	public List<Vertex> getPointsInFront() {
		Iterator<TriangularFacet> it = facesInFront.iterator();
		List<Vertex> l = new ArrayList<Vertex>();
		while (it.hasNext()) {
			l.addAll(it.next().getVerticesInFront());
		}
		return l;
	}

	public List<TriangularFacet> getFacesInFront() {
		return facesInFront;
	}

	public void cleanFaceInFront() {
		facesInFront.clear();
	}

	@Override
	public int numDimensions() {
		return 3;
	}

	@Override
	public void localize(final float[] position) {
		position[0] = getFloatPosition(0);
		position[1] = getFloatPosition(1);
		position[2] = getFloatPosition(2);
	}

	@Override
	public void localize(final double[] position) {
		position[0] = getDoublePosition(0);
		position[1] = getDoublePosition(1);
		position[2] = getDoublePosition(2);
	}

	@Override
	public float getFloatPosition(final int d) {
		switch (d) {
			case 0 :
				return (float) getX();
			case 1 :
				return (float) getY();
			case 2 : 
				return (float) getZ();
			default :
				return -1;
		}
	}

	@Override
	public double getDoublePosition(final int d) {
		switch (d) {
			case 0 :
				return getX();
			case 1 :
				return getY();
			case 2 : 
				return getZ();
			default :
				return -1;
		}
	}
}
