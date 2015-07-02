
package net.imagej.ops.geometric.twod;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.geometric.PolytopCursor;
import net.imglib2.IterableInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.type.logic.BoolType;

/**
 * The {@link Contour} of a Region.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
public class Contour extends Abstract2DPolytop implements
	IterableInterval<BoolType>
{

	private static final int NUM_DIMENSIONS = 2;

	/**
	 * Creates an <em>2</em>-dimensional {@link Contour} with min and max = 0
	 * <sup>2</sup>.
	 */
	public Contour() {
		super(new ArrayList<RealLocalizable>());
	}

	/**
	 * Creates an <em>2</em>-dimensional {@link Contour}.
	 */
	public Contour(final List<RealLocalizable> points) {
		super(points);
	}

	@Override
	public PolytopCursor<BoolType> cursor() {
		return new PolytopCursor<BoolType>(this.vertices());
	}

	@Override
	public PolytopCursor<BoolType> localizingCursor() {
		return cursor();
	}

	@Override
	public long size() {
		return this.vertices().size();
	}

	@Override
	public BoolType firstElement() {
		return cursor().next();
	}

	@Override
	public Object iterationOrder() {
		return this;
	}

	@Override
	public int numDimensions() {
		return NUM_DIMENSIONS;
	}

	@Override
	public PolytopCursor<BoolType> iterator() {
		return this.cursor();
	}
}
