
package net.imagej.ops.geometric.twod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.imagej.Extents;
import net.imagej.Position;
import net.imglib2.AbstractCursor;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.Localizable;
import net.imglib2.Point;
import net.imglib2.Positionable;
import net.imglib2.RandomAccess;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPositionable;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.logic.BoolType;
import net.imglib2.util.Intervals;

/**
 * Contour Rasterizer. Uses the Bresenham algorithm to draw a line between each
 * vertex of the contour.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * @see "https://de.wikipedia.org/wiki/Bresenham-Algorithmus#Kompakte_Variante"
 */
public class RasterizedContour implements IterableRegion<BoolType> {

	private final Contour c;

	public RasterizedContour(final Contour c) {
		this.c = c;
	}

	@Override
	public Cursor<BoolType> cursor() {
		return new RasterizedContourCursor(this.c);
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
		return this.c.realMin(d);
	}

	@Override
	public void realMin(final double[] min) {
		this.c.realMin(min);
	}

	@Override
	public void realMin(final RealPositionable min) {
		this.c.realMin(min);
	}

	@Override
	public double realMax(final int d) {
		return this.c.realMax(d);
	}

	@Override
	public void realMax(final double[] max) {
		this.c.realMax(max);
	}

	@Override
	public void realMax(final RealPositionable max) {
		this.c.realMax(max);
	}

	@Override
	public int numDimensions() {
		return this.c.numDimensions();
	}

	@Override
	public Iterator<BoolType> iterator() {
		return cursor();
	}

	@Override
	public long min(final int d) {
		return this.c.min(d);
	}

	@Override
	public void min(final long[] min) {
		this.c.min(min);
	}

	@Override
	public void min(final Positionable min) {
		this.c.min(min);
	}

	@Override
	public long max(final int d) {
		return this.c.max(d);
	}

	@Override
	public void max(final long[] max) {
		this.c.max(max);
	}

	@Override
	public void max(final Positionable max) {
		this.c.max(max);
	}

	@Override
	public void dimensions(final long[] dimensions) {
		this.c.dimensions(dimensions);
	}

	@Override
	public long dimension(final int d) {
		return this.c.dimension(d);
	}

	@Override
	public RandomAccess<BoolType> randomAccess() {
		return new RasterizedContourRandomAcces(this.c);
	}

	@Override
	public RandomAccess<BoolType> randomAccess(final Interval interval) {
		return new RasterizedContourRandomAcces(this.c, interval);
	}

	private static final class RasterizedContourCursor extends
		AbstractCursor<BoolType>
	{

		private final Contour c;
		private final List<Localizable> outline;
		private int currentIndex;

		public RasterizedContourCursor(final Contour c) {
			super(c.numDimensions());
			this.c = c;
			this.outline = getRasterizedOutline(c.vertices());
		}

		@Override
		public BoolType get() {
			// always return true since we're only iterating over the contour
			return new BoolType(true);
		}

		@Override
		public void fwd() {
			this.currentIndex++;
		}

		@Override
		public void reset() {
			this.currentIndex = 0;
		}

		@Override
		public boolean hasNext() {
			return this.currentIndex < this.outline.size() - 1;
		}

		@Override
		public void localize(final long[] position) {
			this.outline.get(this.currentIndex).localize(position);
		}

		@Override
		public long getLongPosition(final int d) {
			return this.outline.get(this.currentIndex).getLongPosition(d);
		}

		@Override
		public AbstractCursor<BoolType> copy() {
			return new RasterizedContourCursor(this.c);
		}

		@Override
		public AbstractCursor<BoolType> copyCursor() {
			return copy();
		}

		private List<Localizable> getRasterizedOutline(
			final List<RealLocalizable> vertices)
		{

			final ArrayList<Localizable> tmp = new ArrayList<Localizable>();

			for (int i = 0; i < vertices.size(); i++) {

				long x0 = Math.round(vertices.get(i).getDoublePosition(0));
				long y0 = Math.round(vertices.get(i).getDoublePosition(1));
				final long x1 = Math.round(vertices.get((i + 1) % vertices.size())
					.getDoublePosition(0));
				final long y1 = Math.round(vertices.get((i + 1) % vertices.size())
					.getDoublePosition(1));

				final long dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
				final long dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
				long err = dx + dy, e2; /* error value e_xy */

				while (true) {
					tmp.add(new Point(x0, y0));
					if (x0 == x1 && y0 == y1) break;
					e2 = 2 * err;
					if (e2 > dy) {
						err += dy;
						x0 += sx;
					} /* e_xy+e_x > 0 */
					if (e2 < dx) {
						err += dx;
						y0 += sy;
					} /* e_xy+e_y < 0 */
				}
			}

			return tmp;
		}
	}

	private static final class RasterizedContourRandomAcces extends Position
		implements RandomAccess<BoolType>
	{

		private final Contour c;
		private Set<HashablePoint> outline;

		public RasterizedContourRandomAcces(final Contour c) {
			super(new Extents(Intervals.minAsLongArray(c), Intervals.maxAsLongArray(
				c)));

			this.c = c;
			this.outline = getRasterizedOutline(c.vertices());
		}

		public RasterizedContourRandomAcces(final Contour c,
			final Interval interval)
		{
			super(new Extents(Intervals.minAsLongArray(interval), Intervals
				.maxAsLongArray(interval)));

			this.c = c;
			this.outline = getRasterizedOutline(c.vertices());
		}

		@Override
		public BoolType get() {
			final long[] pos = new long[this.numDimensions()];
			this.localize(pos);
			final HashablePoint point = new HashablePoint(pos);
			return new BoolType(this.outline.contains(point));
		}

		@Override
		public RasterizedContourRandomAcces copy() {
			return new RasterizedContourRandomAcces(this.c);
		}

		@Override
		public RasterizedContourRandomAcces copyRandomAccess() {
			return copy();
		}

		private Set<HashablePoint> getRasterizedOutline(
			final List<RealLocalizable> vertices)
		{

			final Set<HashablePoint> tmp = new HashSet<HashablePoint>();

			for (int i = 0; i < vertices.size() - 1; i++) {

				double x0 = vertices.get(i).getDoublePosition(0);
				double y0 = vertices.get(i).getDoublePosition(1);
				double x1 = vertices.get(i + 1).getDoublePosition(0);
				double y1 = vertices.get(i + 1).getDoublePosition(1);

				if (x1 < x0) {
					double a = x0;
					x0 = x1;
					x1 = a;

					a = y0;
					y0 = y1;
					y1 = a;
				}

				final double m = (y1 - y0) / (x1 - x0);
				if (m < 1) {
					double y = y0; // initial value
					for (long x = Math.round(x0); x <= x1; x++, y += m) {
						tmp.add(new HashablePoint(x, Math.round(y)));
					}
				}
				else {
					double x = x0; // initial value
					for (long y = Math.round(y0); y <= y1; y++, x += 1 / m)
						tmp.add(new HashablePoint(Math.round(x), y));
				}
			}

			return tmp;
		}

		private class HashablePoint extends Point {

			public HashablePoint(final long... position) {
				super(position, true);
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + Arrays.hashCode(this.position);
				return result;
			}

			@Override
			public boolean equals(final Object obj) {
				if (this == obj) return true;
				if (obj == null) return false;
				if (getClass() != obj.getClass()) return false;
				final HashablePoint other = (HashablePoint) obj;
				if (!Arrays.equals(this.position, other.position)) return false;
				return true;
			}
		}
	}
}
