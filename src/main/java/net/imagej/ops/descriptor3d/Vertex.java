package net.imagej.ops.descriptor3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import net.imglib2.RealLocalizable;

/**
 * Represents vertices of the hull, as well as the points from which it is
 * formed.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
public class Vertex extends Vector3D implements RealLocalizable {

	private double m_distanceToFaceInFront = 0;
	
	private List<TriangularFacet> m_facesInFront = new ArrayList<TriangularFacet>();
	
	public Vertex(double x, double y, double z) {
		super(x, y, z);
	}

	public double getDistanceToFaceInFront() {
		return m_distanceToFaceInFront;
	}

	public void setDistanceToFaceInFront(double m_distanceToFaceInFront) {
		this.m_distanceToFaceInFront = m_distanceToFaceInFront;
	}

	public void addFaceInFront(TriangularFacet f) {
		m_facesInFront.add(f);
	}
	
	public void clearFacesInFront() {
		m_facesInFront.clear();
	}
	
	
	public List<Vertex> getPointsInFront() {
		Iterator<TriangularFacet> it = m_facesInFront.iterator();
		List<Vertex> l = new ArrayList<Vertex>();
		while (it.hasNext()) {
			l.addAll(it.next().getVerticesInFront());
		}
		return l;
	}

	public List<TriangularFacet> getFacesInFront() {
		return m_facesInFront;
	}

	public void cleanFaceInFront() {
		m_facesInFront.clear();
	}

	@Override
	public int numDimensions() {
		return 3;
	}

	@Override
	public void localize(float[] position) {
		position[0] = getFloatPosition(0);
		position[1] = getFloatPosition(1);
		position[2] = getFloatPosition(2);
	}

	@Override
	public void localize(double[] position) {
		position[0] = getDoublePosition(0);
		position[1] = getDoublePosition(1);
		position[2] = getDoublePosition(2);
	}

	@Override
	public float getFloatPosition(int d) {
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
	public double getDoublePosition(int d) {
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
