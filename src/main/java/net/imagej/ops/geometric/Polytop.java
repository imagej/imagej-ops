
package net.imagej.ops.geometric;

import java.util.List;

import net.imglib2.Interval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealRandomAccessibleRealInterval;
import net.imglib2.type.logic.BoolType;

/**
 * A polytope is a geometric object with flat sides, and may exist in any
 * general number of dimensions n as an n-dimensional polytope.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
public interface Polytop extends RealRandomAccessibleRealInterval<BoolType>,
	Interval
{

	/**
	 * @return all vertices of the polytop.
	 */
	public List<RealLocalizable> vertices();

	/**
	 * @param rl a {@link RealLocalizable}
	 * @return true if the {@link Polytop} contains the given
	 *         {@link RealLocalizable}
	 */
	public boolean contains(RealLocalizable rl);

}
