
package net.imagej.ops.geometric.twod;

import java.util.ArrayList;
import java.util.List;

import net.imglib2.RealLocalizable;

/**
 * Marker class for convex {@link Polygon}s
 * 
 * @author Christian Dietz, University of Konstanz
 * @author Daniel Seebacher, University of Konstanz
 */
public class ConvexPolygon extends Polygon {

	public ConvexPolygon(final List<RealLocalizable> points) {
		super(points);
	}

	public ConvexPolygon() {
		this(new ArrayList<RealLocalizable>());
	}
}
