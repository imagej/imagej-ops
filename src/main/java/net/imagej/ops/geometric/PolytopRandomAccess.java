
package net.imagej.ops.geometric;

import net.imglib2.AbstractRealLocalizable;
import net.imglib2.Localizable;
import net.imglib2.RandomAccess;
import net.imglib2.RealInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.RealRandomAccess;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BoolType;
import net.imglib2.util.Intervals;

/**
 * A general {@link RandomAccess} for a {@link Polytop}. Checks whether the a
 * point is inside the polygon or not.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * @param <T>
 */
public final class PolytopRandomAccess<T extends BooleanType<T>> extends
	AbstractRealLocalizable implements RealRandomAccess<T>
{

	private final RealInterval bounds;
	private final Polytop p;

	public PolytopRandomAccess(final RealInterval bounds, final Polytop p) {
		// polygon is only defined in two dimensions
		super(bounds.numDimensions());

		this.bounds = bounds;
		this.p = p;
	}

	/**
	 * Return true if the given point is contained inside the boundary. See:
	 * http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
	 */
	public boolean contains(final RealLocalizable localizable) {
		if (localizable.numDimensions() == this.numDimensions() && Intervals
			.contains(this.bounds, localizable))
		{
			return this.p.contains(localizable);
		}
		return false;
	}

	@Override
	public void fwd(final int d) {
		super.position[d]++;
	}

	@Override
	public void bck(final int d) {
		super.position[d]--;
	}

	@Override
	public void move(final int distance, final int d) {
		super.position[d] += distance;
	}

	@Override
	public void move(final long distance, final int d) {
		super.position[d] += distance;
	}

	@Override
	public void move(final Localizable localizable) {
		for (int d = 0; d < this.n; ++d) {
			super.position[d] += localizable.getDoublePosition(d);
		}
	}

	@Override
	public void move(final int[] distance) {
		for (int d = 0; d < this.n; ++d) {
			super.position[d] += distance[d];
		}
	}

	@Override
	public void move(final long[] distance) {
		for (int d = 0; d < this.n; ++d) {
			super.position[d] += distance[d];
		}
	}

	@Override
	public void setPosition(final Localizable localizable) {
		for (int d = 0; d < this.n; ++d) {
			super.position[d] = localizable.getDoublePosition(d);
		}
	}

	@Override
	public void setPosition(final int[] pos) {
		for (int d = 0; d < this.n; ++d) {
			super.position[d] = pos[d];
		}
	}

	@Override
	public void setPosition(final long[] pos) {
		for (int d = 0; d < this.n; ++d) {
			super.position[d] = pos[d];
		}
	}

	@Override
	public void setPosition(final int position, final int d) {
		super.position[d] = position;
	}

	@Override
	public void setPosition(final long position, final int d) {
		super.position[d] = position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get() {
		final boolean contains = contains(new RealPoint(super.position));
		return (T) new BoolType(contains);
	}

	@Override
	public PolytopRandomAccess<T> copy() {
		return new PolytopRandomAccess<T>(this.bounds, this.p);
	}

	@Override
	public RealRandomAccess<T> copyRealRandomAccess() {
		return copy();
	}

	@Override
	public void move(final float distance, final int d) {
		super.position[d] += distance;
	}

	@Override
	public void move(final double distance, final int d) {
		super.position[d] += distance;
	}

	@Override
	public void move(final RealLocalizable localizable) {
		for (int d = 0; d < this.n; ++d) {
			super.position[d] += localizable.getDoublePosition(d);
		}
	}

	@Override
	public void move(final float[] distance) {
		for (int d = 0; d < this.n; ++d) {
			super.position[d] += distance[d];
		}
	}

	@Override
	public void move(final double[] distance) {
		for (int d = 0; d < this.n; ++d) {
			super.position[d] += distance[d];
		}
	}

	@Override
	public void setPosition(final RealLocalizable localizable) {
		for (int d = 0; d < this.n; ++d) {
			super.position[d] = localizable.getDoublePosition(d);
		}
	}

	@Override
	public void setPosition(final float[] pos) {
		for (int d = 0; d < this.n; ++d) {
			super.position[d] = pos[d];
		}
	}

	@Override
	public void setPosition(final double[] pos) {
		for (int d = 0; d < this.n; ++d) {
			super.position[d] = pos[d];
		}
	}

	@Override
	public void setPosition(final float pos, final int d) {
		super.position[d] = pos;
	}

	@Override
	public void setPosition(final double pos, final int d) {
		super.position[d] = pos;
	}
}
