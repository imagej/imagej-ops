
package net.imagej.ops.geometric.twod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
 * Polygon Rasterizer. Uses the algorithm from
 * http://alienryderflex.com/polygon_fill/.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
public class RasterizedPolygon implements IterableRegion<BoolType> {

	private Polygon p;

	private final Map<Integer, List<Integer>> scanline;

	public RasterizedPolygon(final Polygon p) {
		this.p = p;

		this.scanline = createScanLine(p);
	}

	private Map<Integer, List<Integer>> createScanLine(final Polygon p2) {

		final Map<Integer, List<Integer>> scl =
			new HashMap<Integer, List<Integer>>();

		for (int y = (int) Math.floor(p2.min(1)); y < Math.ceil(p2.max(1)); y++) {
			// Build a list of nodes.

			final List<Integer> xValues = new ArrayList<Integer>();
			int j = p2.vertices().size() - 1;
			for (int i = 0; i < p2.vertices().size(); i++) {

				final double polyY_i = p2.vertices().get(i).getDoublePosition(1);
				final double polyX_i = p2.vertices().get(i).getDoublePosition(0);
				final double polyY_j = p2.vertices().get(j).getDoublePosition(1);
				final double polyX_j = p2.vertices().get(j).getDoublePosition(0);

				if (polyY_i < y && polyY_j >= y || polyY_j < y && polyY_i >= y) {
					xValues.add((int) (polyX_i + (y - polyY_i) / (polyY_j - polyY_i) *
						(polyX_j - polyX_i)));
				}
				j = i;
			}

			// Sort the nodes, via a simple “Bubble” sort.
			Collections.sort(xValues);

			if (!xValues.isEmpty()) {
				scl.put(y, xValues);
			}
		}

		return scl;
	}

	@Override
	public Cursor<BoolType> cursor() {
		return new RasterizedPolygonCursor(this.p, this.scanline);
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
		return new RasterizedPolygonRandomAcces(this.p, this.scanline);
	}

	@Override
	public RandomAccess<BoolType> randomAccess(final Interval interval) {
		return new RasterizedPolygonRandomAcces(interval, this.scanline);
	}

	private static final class RasterizedPolygonCursor extends
		AbstractCursor<BoolType>
	{

		private final Interval p;
		private final Map<Integer, List<Integer>> scl;

		private long[] maxAsLongArray;
		private long[] minAsLongArray;

		private Point currentPosition;
		private BoolType currentValue;

		public RasterizedPolygonCursor(final Interval p,
			final Map<Integer, List<Integer>> scanline)
		{
			super(p.numDimensions());

			this.p = p;

			this.minAsLongArray = Intervals.minAsLongArray(p);
			this.maxAsLongArray = Intervals.maxAsLongArray(p);

			this.scl = scanline;

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

			final List<Integer> list = this.scl.get(this.currentPosition
				.getIntPosition(1));
			if (list == null) {
				this.currentValue = new BoolType(false);
				return;
			}

			final int currentx = this.currentPosition.getIntPosition(0);
			for (int i = 0; i < list.size(); i += 2) {
				final int x0 = list.get(i);
				final int x1 = list.get(i + 1);

				if (currentx >= x0 && currentx <= x1) {
					this.currentValue = new BoolType(true);
					return;
				}
			}

			this.currentValue = new BoolType(false);
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
			return new RasterizedPolygonCursor(this.p, this.scl);
		}

		@Override
		public AbstractCursor<BoolType> copyCursor() {
			return copy();
		}
	}

	private static final class RasterizedPolygonRandomAcces extends Position
		implements RandomAccess<BoolType>
	{

		private final Interval interval;
		private Map<Integer, List<Integer>> scl;

		public RasterizedPolygonRandomAcces(final Interval p,
			final Map<Integer, List<Integer>> scanline)
		{
			super(new Extents(Intervals.minAsLongArray(p), Intervals.maxAsLongArray(
				p)));

			this.interval = p;
			this.scl = scanline;
		}

		@Override
		public BoolType get() {

			final long[] pos = new long[numDimensions()];
			this.localize(pos);

			final int currenty = this.getIntPosition(this.getIntPosition(1));

			final List<Integer> list = this.scl.get(currenty);
			if (list == null) {
				return new BoolType(false);
			}

			final int currentx = this.getIntPosition(this.getIntPosition(0));
			for (int i = 0; i < list.size(); i += 2) {
				final int x0 = list.get(i);
				final int x1 = list.get(i + 1);

				if (currentx >= x0 && currentx <= x1) {
					return new BoolType(true);
				}
			}

			return new BoolType(false);
		}

		@Override
		public RasterizedPolygonRandomAcces copy() {
			return new RasterizedPolygonRandomAcces(this.interval, this.scl);
		}

		@Override
		public RasterizedPolygonRandomAcces copyRandomAccess() {
			return copy();
		}

	}
}
