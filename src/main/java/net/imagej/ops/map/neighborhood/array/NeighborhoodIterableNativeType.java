
package net.imagej.ops.map.neighborhood.array;

import java.util.Iterator;

import net.imglib2.type.NativeType;

/**
 * Optimized rectangle neighborhood {@link Iterable} for {@link NativeType}
 * which ignores out of bounds pixels.
 * 
 * @param <I> Type of the contents of the Iterable.
 * @author Jonathan Hale
 */
final class NeighborhoodIterableNativeType<I extends NativeType<I>> implements
	Iterable<I>
{

	private final I pointer;
	private final int neighSize;
	private final int hDiameter;
	private final int vDiameter;
	private final int startIndex;
	private final int nextLineSkip;
	private final int nextSliceSkip;

	/* whether this is a 3D neighborhood */
	private final boolean volume;

	/**
	 * Constructor
	 * 
	 * @param pointer NativeType to use as "cursor"
	 * @param x Left bounds of the rectangle neighborhood
	 * @param y Top bounds of the rectangle neighborhood
	 * @param z Front bounds of the rectangle neighborhood
	 * @param w Width of the rectangle neighborhood
	 * @param h Height of the rectangle neighborhood
	 * @param d Depth of the rectangle neighborhood
	 * @param span Span of the neighborhood (to avoid redundant calculation)
	 */
	public NeighborhoodIterableNativeType(final I pointer, final int x,
		final int y, final int z, final int w, final int h, final int d,
		final int span)
	{
		// clamp extensions in every direction to ensure we won't go out of bounds
		final int left = Math.min(x, span);
		final int top = Math.min(y, span);
		final int right = Math.min(w - 1 - x, span);
		final int bottom = Math.min(h - 1 - y, span);
		final int front = Math.min(z, span);
		final int back = Math.min(d - 1 - z, span);

		this.hDiameter = left + right + 1;
		this.vDiameter = top + bottom + 1;
		final int dDiameter = back + front + 1;
		this.pointer = pointer;
		this.neighSize = hDiameter * vDiameter * dDiameter;

		pointer.decIndex(front * w * h + top * w + left + 1);

		this.startIndex = pointer.getIndex();

		this.nextLineSkip = w - (hDiameter - 1);
		this.nextSliceSkip = (h - vDiameter) * hDiameter;

		this.volume = vDiameter != 1;
	}

	@Override
	public final Iterator<I> iterator() {
		return (volume) ? new MapNeighborhoodIterator3D()
			: new MapNeighborhoodIterator2D();
	}

	/**
	 * Iterator over a rectangular neighborhood.
	 * 
	 * @author Jonathan Hale
	 */
	private final class MapNeighborhoodIterator2D implements Iterator<I> {

		public MapNeighborhoodIterator2D() {
			pointer.updateIndex(startIndex);
		}

		int index = 0;
		int x = -1;

		@Override
		public final boolean hasNext() {
			return index < neighSize;
		}

		@Override
		public final I next() {
			++index;
			++x;

			if (x == hDiameter) {
				// end of line, skip pixels until next line
				pointer.incIndex(nextLineSkip);
				x = 0;
			}
			else {
				pointer.incIndex();
			}

			return pointer;
		}
	}

	/**
	 * Iterator over a rectangular neighborhood.
	 * 
	 * @author Jonathan Hale
	 */
	private final class MapNeighborhoodIterator3D implements Iterator<I> {

		public MapNeighborhoodIterator3D() {
			pointer.updateIndex(startIndex);
		}

		int index = 0;
		int x = -1;
		int y = -1;

		@Override
		public final boolean hasNext() {
			return index < neighSize;
		}

		@Override
		public final I next() {
			++index;
			++x;

			if (x == hDiameter) {
				// end of line, skip pixels until next line
				pointer.incIndex(nextLineSkip);
				x = 0;
				++y;
				if (y == vDiameter) {
					// end of slice, skip pixels until next slice
					pointer.incIndex(nextSliceSkip);
					y = 0;
				}
			}
			else {
				pointer.incIndex();
			}

			return pointer;
		}

		@Override
		public final void remove() {
			// noop
		}
	}
}
