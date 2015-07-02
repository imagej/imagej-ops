
package net.imagej.ops.geometric.twod;

import java.util.Arrays;
import java.util.Iterator;

import net.imagej.Extents;
import net.imagej.Position;
import net.imglib2.AbstractCursor;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.Point;
import net.imglib2.Positionable;
import net.imglib2.RandomAccess;
import net.imglib2.RealPositionable;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.logic.BoolType;
import net.imglib2.util.Intervals;

/**
 * Polygon Rasterizer. FIXME: currently using the worst possible algorithm to
 * rasterize the polygon. for every point from min to max a point in polygon
 * test is made.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
public class RasterizedPolygon implements IterableRegion<BoolType> {

	private Polygon p;

	public RasterizedPolygon(final Polygon p) {
		this.p = p;
	}

	@Override
	public Cursor<BoolType> cursor() {
		return new RasterizedPolygonCursor(this.p);
	}

	@Override
	public Cursor<BoolType> localizingCursor() {
		return cursor();
	}

	@Override
	public long size() {
		return 0;
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
	public double realMin(final int d) {
		return this.p.realMin(d);
	}

	@Override
	public void realMin(final double[] min) {
		this.p.realMin(min);
	}

	@Override
	public void realMin(final RealPositionable min) {
		this.p.realMin(min);
	}

	@Override
	public double realMax(final int d) {
		return this.p.realMax(d);
	}

	@Override
	public void realMax(final double[] max) {
		this.p.realMax(max);
	}

	@Override
	public void realMax(final RealPositionable max) {
		this.p.realMax(max);
	}

	@Override
	public int numDimensions() {
		return this.p.numDimensions();
	}

	@Override
	public Iterator<BoolType> iterator() {
		return cursor();
	}

	@Override
	public long min(final int d) {
		return this.p.min(d);
	}

	@Override
	public void min(final long[] min) {
		this.p.min(min);
	}

	@Override
	public void min(final Positionable min) {
		this.p.min(min);
	}

	@Override
	public long max(final int d) {
		return this.p.max(d);
	}

	@Override
	public void max(final long[] max) {
		this.p.max(max);
	}

	@Override
	public void max(final Positionable max) {
		this.p.max(max);
	}

	@Override
	public void dimensions(final long[] dimensions) {
		this.p.dimensions(dimensions);
	}

	@Override
	public long dimension(final int d) {
		return this.p.dimension(d);
	}

	@Override
	public RandomAccess<BoolType> randomAccess() {
		return new RasterizedPolygonRandomAcces(this.p);
	}

	@Override
	public RandomAccess<BoolType> randomAccess(final Interval interval) {
		return new RasterizedPolygonRandomAcces(this.p, interval);
	}

	private static final class RasterizedPolygonCursor extends
		AbstractCursor<BoolType>
	{

		private final Polygon p;

		private long[] maxAsLongArray;
		private long[] minAsLongArray;

		private Point currentPosition;
		private BoolType currentValue;

		public RasterizedPolygonCursor(final Polygon p) {
			super(p.numDimensions());

			this.p = p;

			this.minAsLongArray = Intervals.minAsLongArray(p);
			this.maxAsLongArray = Intervals.maxAsLongArray(p);

			this.currentPosition = null;
			this.currentValue = null;
		}

		@Override
		public BoolType get() {
			return this.currentValue;
		}

		@Override
		public void fwd() {

			if (this.currentValue == null) {
				this.currentPosition = new Point(this.minAsLongArray);
			}
			else {
				this.currentPosition.fwd(0);
				for (int d = 0; d < numDimensions() - 1; d++) {
					if (this.currentPosition.getLongPosition(
						d) >= this.maxAsLongArray[d])
					{
						this.currentPosition.setPosition(this.minAsLongArray[d], d);
						this.currentPosition.move(1, d + 1);
					}
				}
			}

			this.currentValue = new BoolType(this.p.contains(this.currentPosition));
		}

		@Override
		public void reset() {
			this.currentPosition = null;
			this.currentValue = null;
		}

		@Override
		public boolean hasNext() {

			if (this.currentPosition == null) {
				return true;
			}

			final long[] currentPos = new long[numDimensions()];
			localize(currentPos);
			for (int i = 0; i < numDimensions(); i++) {
				currentPos[i] += 1;
			}

			if (!Arrays.equals(currentPos, this.maxAsLongArray)) {
				return true;
			}

			return false;
		}

		@Override
		public void localize(final long[] position) {
			this.currentPosition.localize(position);
		}

		@Override
		public long getLongPosition(final int d) {
			return this.currentPosition.getLongPosition(d);
		}

		@Override
		public AbstractCursor<BoolType> copy() {
			return new RasterizedPolygonCursor(this.p);
		}

		@Override
		public AbstractCursor<BoolType> copyCursor() {
			return copy();
		}
	}

	private static final class RasterizedPolygonRandomAcces extends Position
		implements RandomAccess<BoolType>
	{

		private final Polygon p;

		public RasterizedPolygonRandomAcces(final Polygon p) {
			super(new Extents(Intervals.minAsLongArray(p), Intervals.maxAsLongArray(
				p)));

			this.p = p;
		}

		public RasterizedPolygonRandomAcces(final Polygon p,
			final Interval interval)
		{
			super(new Extents(Intervals.minAsLongArray(interval), Intervals
				.maxAsLongArray(interval)));

			this.p = p;
		}

		@Override
		public BoolType get() {

			final long[] pos = new long[numDimensions()];
			this.localize(pos);

			return new BoolType(this.p.contains(new Point(pos)));
		}

		@Override
		public RasterizedPolygonRandomAcces copy() {
			return new RasterizedPolygonRandomAcces(this.p);
		}

		@Override
		public RasterizedPolygonRandomAcces copyRandomAccess() {
			return copy();
		}

	}
}
