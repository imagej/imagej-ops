
package net.imagej.ops.geometric.twod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.imglib2.RealLocalizable;

/**
 * Polygon class.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
public class Polygon extends Abstract2DPolytop implements
	Iterable<RealLocalizable>
{

	private static final int NUM_DIMENSIONS = 2;

	public Polygon(final List<RealLocalizable> points) {
		super(points);
	}

	public Polygon() {
		this(new ArrayList<RealLocalizable>());
	}

	@Override
	public Iterator<RealLocalizable> iterator() {
		return this.vertices().iterator();
	}

	@Override
	public int numDimensions() {
		return NUM_DIMENSIONS;
	}
}
