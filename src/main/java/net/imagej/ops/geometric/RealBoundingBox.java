
package net.imagej.ops.geometric;

import net.imglib2.AbstractRealInterval;
import net.imglib2.RealLocalizable;

/**
 * Simple Bounding Box with real coordinates.
 * 
 * @author Christian Dietz, University of Konstanz.
 * @author Daniel Seebacher, University of Konstanz.
 */
public class RealBoundingBox extends AbstractRealInterval {

	public RealBoundingBox(final int n) {
		super(n);
	}

	/**
	 * update the minimum and maximum extents with the given coordinates.
	 *
	 * @param position
	 */
	public void update(final RealLocalizable position) {
		for (int d = 0; d < this.min.length; d++) {
			final double p = position.getDoublePosition(d);
			if (p < this.min[d]) this.min[d] = p;
			if (p > this.max[d]) this.max[d] = p;
		}
	}

	/**
	 * update the minimum and maximum extents with the given coordinates.
	 *
	 * @param position
	 */
	public void update(final double[] position) {
		for (int d = 0; d < this.min.length; d++) {
			if (position[d] < this.min[d]) this.min[d] = position[d];
			if (position[d] > this.max[d]) this.max[d] = position[d];
		}
	}
}
