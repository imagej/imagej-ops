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
import java.util.List;

import net.imglib2.RealLocalizable;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/**
 * Represents vertices of the hull, as well as the points from which it is
 * formed.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
public class Vertex implements RealLocalizable {
	public static final Vertex ZERO   = new Vertex(0, 0, 0);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double distanceToFaceInFront = 0;
	
	private List<TriangularFacet> facesInFront = new ArrayList<>();
	
	protected double x;
	
	protected double y;
	
	protected double z;
	
	public Vertex(final double x, final double y, final double z) {
		//super(x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
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
		List<Vertex> l = new ArrayList<>();
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
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getZ() {
		return this.z;
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
	
	public Vertex add(Vertex v)
	{
	    return new Vertex(this.getX() + v.getX(), this.getY() + v.getY(), this.getZ() + v.getZ());
	}
	
	public Vertex setDoublePosition(final int d, final double val) {
		switch (d) {
			case 0 :
				return this.add(new Vertex(val-getX(), 0, 0));
			case 1 :
				return this.add(new Vertex(0, val-getY(), 0));
			case 2 : 
				return this.add(new Vertex(0, 0, val-getZ()));
			default :
				return new Vertex(0, 0, 0);
		}
	}
	
	/**
	 * A Mutable way of changing a Vertex
	 * 
	 * @param d
	 * @param val
	 * @return
	 */
	public Vertex changeDoublePosition(final int d, final double val) {
		switch (d) {
			case 0 :
				this.x = val;
				break;
			case 1 :
				this.y = val;
				break;
			case 2 : 
				this.z = val;
				break;
		}
		return this;
	}
	
	public Vertex subtract(Vertex v) {
		return new Vertex( this.getX() - v.getX(), this.getY() - v.getY(), this.getZ() - v.getZ() );
	}
	
	public Vertex crossProduct(Vertex v) {
		return new Vertex( MathArrays.linearCombination(y, v.getZ(), -z, v.getY()), 
						   MathArrays.linearCombination(z, v.getX(), -x, v.getZ()), 
						   MathArrays.linearCombination(x, v.getY(), -y, v.getX()) );
	}
	
	public double getNorm() {
		return FastMath.sqrt (x * x + y * y + z * z);
	}
	
	public double getNormSq() {
		return ( x * x + y * y + z * z );
	}
	
	public Vertex scalarMultiply( double scalar ) {
		return new Vertex( this.getX() * scalar,
						   this.getY() * scalar,
						   this.getZ() * scalar );
	}
	
	public Vertex normalize() throws Exception {
		double s = getNorm();
        if (s == 0) {
            throw new Exception("Cannot normalize a zero norm vector");
        }
        return scalarMultiply(1 / s);
	}
	
	public double dotProduct( Vertex v ) {
		return MathArrays.linearCombination(x, v.getX(), y, v.getY(), z, v.getZ());
	}	
	
	// Should we just use an underlying array, to ensure consistency in mutability?
	public double[] toArray() {
		return new double[]{ x, y, z };
	}
	
	
	public boolean equals( Object other ) {
		return (other instanceof Vertex) && 
				( this.x == ((Vertex) other).x && 
				  this.y == ((Vertex) other).y && 
				  this.z == ((Vertex) other).z  );
	}
	
	public int hashCode() {
        if ( Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z) ) {
            return 642;
        }
        return 643 * (164 * MathUtils.hash(x) +  3 * MathUtils.hash(y) +  MathUtils.hash(z));
        // Maybe check facesInFront too?
    }
}
