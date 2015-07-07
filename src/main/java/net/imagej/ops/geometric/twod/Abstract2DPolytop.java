
package net.imagej.ops.geometric.twod;

import java.util.ArrayList;
import java.util.List;

import net.imagej.ops.geometric.Polytop;
import net.imagej.ops.geometric.PolytopRandomAccess;
import net.imglib2.Positionable;
import net.imglib2.RealInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPositionable;
import net.imglib2.RealRandomAccess;
import net.imglib2.type.logic.BoolType;
import net.imglib2.util.Intervals;

/**
 * Abstract 2D Polytop.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
public abstract class Abstract2DPolytop implements Polytop {

	private final double[] min;
	private final double[] max;
	private final List<RealLocalizable> vertices;

	protected Abstract2DPolytop(final List<RealLocalizable> vertices) {
		this.min = new double[numDimensions()];
		this.max = new double[numDimensions()];
		this.vertices = new ArrayList<RealLocalizable>();

		for (final RealLocalizable rl : vertices) {
			this.add(rl);
		}
	}

	@Override
	public List<RealLocalizable> vertices() {
		return this.vertices;
	}

	private void add(final RealLocalizable p) {
		if (p.numDimensions() != numDimensions()) {
			throw new IllegalArgumentException(
				"Polygon supports only two dimensions");
		}

		this.vertices.add(p);
		update(p);
	}

	/**
	 * Updates min/max of the interval
	 * 
	 * @param localizable used to update min/max
	 */
	private void update(final RealLocalizable localizable) {
		{
			for (int d = 0; d < numDimensions(); d++) {
				final double p = localizable.getDoublePosition(d);
				if (p < this.min[d]) this.min[d] = p;
				if (p > this.max[d]) this.max[d] = p;
			}
		}
	}

	/**
	 * Return true if the given point is contained inside the boundary. See:
	 * http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
	 */
	@Override
	public boolean contains(final RealLocalizable localizable) {
		if (localizable.numDimensions() == 2 && Intervals.contains(this,
			localizable))
		{
			int i;
			int j;
			boolean result = false;
			for (i = 0, j = this.vertices.size() - 1; i < this.vertices.size(); j =
				i++)
			{
				if ((this.vertices.get(i).getDoublePosition(1) > localizable
					.getDoublePosition(1)) != (this.vertices.get(j).getDoublePosition(
						1) > localizable.getDoublePosition(1)) && (localizable
							.getDoublePosition(0) < (this.vertices.get(j).getDoublePosition(
								0) - this.vertices.get(i).getDoublePosition(0)) * (localizable
									.getDoublePosition(1) - this.vertices.get(i)
										.getDoublePosition(1)) / (this.vertices.get(j)
											.getDoublePosition(1) - this.vertices.get(i)
												.getDoublePosition(1)) + this.vertices.get(i)
													.getDoublePosition(0)))
				{
					result = !result;
				}
			}

			return result;
		}
		return false;
	}

	@Override
	public RealRandomAccess<BoolType> realRandomAccess() {
		return new PolytopRandomAccess<BoolType>(this, this);
	}

	@Override
	public RealRandomAccess<BoolType> realRandomAccess(
		final RealInterval interval)
	{
		return new PolytopRandomAccess<BoolType>(interval, this);
	}

	@Override
	public double realMin(final int d) {
		return this.min[d];
	}

	@Override
	public void realMin(final double[] m) {
		System.arraycopy(this.min, 0, m, 0, numDimensions());
	}

	@Override
	public void realMin(final RealPositionable m) {
		m.setPosition(this.min);
	}

	@Override
	public double realMax(final int d) {
		return this.max[d];
	}

	@Override
	public void realMax(final double[] m) {
		System.arraycopy(this.max, 0, m, 0, numDimensions());
	}

	@Override
	public void realMax(final RealPositionable m) {
		m.setPosition(this.max);
	}

	@Override
	public long min(final int d) {
		return (long) this.min[d];
	}

	@Override
	public void min(final long[] minArray) {
		for (int d = 0; d < numDimensions(); d++) {
			minArray[d] = min(d);
		}
	}

	@Override
	public void min(final Positionable minPositionable) {
		for (int d = 0; d < numDimensions(); d++) {
			minPositionable.setPosition(min(d), d);
		}
	}

	@Override
	public long max(final int d) {
		return (long) this.max[d];
	}

	@Override
	public void max(final long[] maxArray) {
		for (int d = 0; d < numDimensions(); d++) {
			maxArray[d] = max(d);
		}
	}

	@Override
	public void max(final Positionable maxPositionable) {
		for (int d = 0; d < numDimensions(); d++) {
			maxPositionable.setPosition(max(d), d);
		}
	}

	@Override
	public void dimensions(final long[] dimensions) {
		for (int d = 0; d < numDimensions(); d++) {
			dimensions[d] = dimension(d);
		}
	}

	@Override
	public long dimension(final int d) {
		return max(d) - min(d);
	}

}
