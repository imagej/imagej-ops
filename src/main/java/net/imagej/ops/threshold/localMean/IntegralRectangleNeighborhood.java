
package net.imagej.ops.threshold.localMean;

import java.util.Iterator;

import net.imglib2.AbstractEuclideanSpace;
import net.imglib2.AbstractLocalizable;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.Positionable;
import net.imglib2.RandomAccess;
import net.imglib2.RealPositionable;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.algorithm.neighborhood.RectangleNeighborhoodFactory;

/**
 * TODO Documentation
 *
 * @author Stefan Helfrich (University of Konstanz)
 * @param <T>
 */
public class IntegralRectangleNeighborhood<T> extends AbstractLocalizable
	implements Neighborhood<T>
{

	public static <T> RectangleNeighborhoodFactory<T> factory() {
		return new RectangleNeighborhoodFactory<T>() {

			@Override
			public Neighborhood<T> create(final long[] position,
				final long[] currentMin, final long[] currentMax, final Interval span,
				final RandomAccess<T> sourceRandomAccess)
			{
				return new IntegralRectangleNeighborhood<>(position, currentMin,
					currentMax, span, sourceRandomAccess);
			}
		};
	}

	private final long[] currentMin;

	private final long[] currentMax;

	private final long[] dimensions;

	private final RandomAccess<T> sourceRandomAccess;

	private final Interval structuringElementBoundingBox;

	private final long maxIndex;

	public IntegralRectangleNeighborhood(final long[] position,
		final long[] currentMin, final long[] currentMax, final Interval span,
		final RandomAccess<T> sourceRandomAccess)
	{
		super(position);
		this.currentMin = currentMin;
		this.currentMax = currentMax;
		dimensions = new long[n];
		span.dimensions(dimensions);

		long mi = dimensions[0];
		for (int d = 1; d < n; ++d)
			mi *= dimensions[d];
		maxIndex = mi;

		this.sourceRandomAccess = sourceRandomAccess;
		this.structuringElementBoundingBox = span;
	}

	/**
	 * @return the sourceRandomAccess
	 */
	public RandomAccess<T> getSourceRandomAccess() {
		return sourceRandomAccess;
	}

	@Override
	public Interval getStructuringElementBoundingBox() {
		return structuringElementBoundingBox;
	}

	@Override
	public long size() {
		return maxIndex; // -1 because we skip the center pixel
	}

	@Override
	public T firstElement() {
		return cursor().next();
	}

	@Override
	public Object iterationOrder() {
		return this; // iteration order is only compatible with ourselves
	}

	@Override
	public double realMin(final int d) {
		return currentMin[d];
	}

	@Override
	public void realMin(final double[] min) {
		for (int d = 0; d < n; ++d)
			min[d] = currentMin[d];
	}

	@Override
	public void realMin(final RealPositionable min) {
		for (int d = 0; d < n; ++d)
			min.setPosition(currentMin[d], d);
	}

	@Override
	public double realMax(final int d) {
		return currentMax[d];
	}

	@Override
	public void realMax(final double[] max) {
		for (int d = 0; d < n; ++d)
			max[d] = currentMax[d];
	}

	@Override
	public void realMax(final RealPositionable max) {
		for (int d = 0; d < n; ++d)
			max.setPosition(currentMax[d], d);
	}

	@Override
	public Iterator<T> iterator() {
		return cursor();
	}

	@Override
	public long min(final int d) {
		return currentMin[d];
	}

	@Override
	public void min(final long[] min) {
		for (int d = 0; d < n; ++d)
			min[d] = currentMin[d];
	}

	@Override
	public void min(final Positionable min) {
		for (int d = 0; d < n; ++d)
			min.setPosition(currentMin[d], d);
	}

	@Override
	public long max(final int d) {
		return currentMax[d];
	}

	@Override
	public void max(final long[] max) {
		for (int d = 0; d < n; ++d)
			max[d] = currentMax[d];
	}

	@Override
	public void max(final Positionable max) {
		for (int d = 0; d < n; ++d)
			max.setPosition(currentMax[d], d);
	}

	@Override
	public void dimensions(final long[] dimensions) {
		for (int d = 0; d < n; ++d)
			dimensions[d] = this.dimensions[d];
	}

	@Override
	public long dimension(final int d) {
		return dimensions[d];
	}

	@Override
	public LocalCursor cursor() {
		return new LocalCursor(sourceRandomAccess.copyRandomAccess());
	}

	@Override
	public LocalCursor localizingCursor() {
		return cursor();
	}

	private enum IntegralPosition {
		NONE,
		A,
		B,
		C,
		D;
	}
	
	public class LocalCursor extends AbstractEuclideanSpace implements Cursor<T> {

		private final RandomAccess<T> source;

		private long index;

		private IntegralPosition currentIntegralPosition;
		
		public LocalCursor(final RandomAccess<T> source) {
			super(source.numDimensions());
			this.source = source;
			reset();
		}

		protected LocalCursor(final LocalCursor c) {
			super(c.numDimensions());
			source = c.source.copyRandomAccess();
			index = c.index;
			currentIntegralPosition = c.currentIntegralPosition;
		}

		@Override
		public T get() {
			return source.get();
		}

		@Override
		public void fwd() {
			switch (currentIntegralPosition) {
				case NONE: source.setPosition(currentMin); currentIntegralPosition = IntegralPosition.A; break;
				case A: source.setPosition(new long[]{currentMax[0]-1, currentMin[1]}); currentIntegralPosition = IntegralPosition.B; break;
				case B: source.setPosition(new long[]{currentMin[0], currentMax[1]-1}); currentIntegralPosition = IntegralPosition.C; break;
				case C: source.setPosition(new long[]{currentMax[0]-1, currentMax[1]-1}); currentIntegralPosition = IntegralPosition.D; break;
				case D: source.setPosition(currentMin); break;
			}
		}

		@Override
		public void jumpFwd(final long steps) {
			// TODO Implement
			for (long i = 0; i < steps; ++i)
				fwd();
		}

		@Override
		public T next() {
			fwd();
			return get();
		}

		@Override
		public void remove() {
			// NB: no action.
		}

		@Override
		public void reset() {
			source.setPosition(currentMin);
			currentIntegralPosition = IntegralPosition.NONE;
			source.bck(0);
			index = 0;
		}

		@Override
		public boolean hasNext() {
			// FIXME
			return index < maxIndex;
		}

		@Override
		public float getFloatPosition(final int d) {
			return source.getFloatPosition(d);
		}

		@Override
		public double getDoublePosition(final int d) {
			return source.getDoublePosition(d);
		}

		@Override
		public int getIntPosition(final int d) {
			return source.getIntPosition(d);
		}

		@Override
		public long getLongPosition(final int d) {
			return source.getLongPosition(d);
		}

		@Override
		public void localize(final long[] position) {
			source.localize(position);
		}

		@Override
		public void localize(final float[] position) {
			source.localize(position);
		}

		@Override
		public void localize(final double[] position) {
			source.localize(position);
		}

		@Override
		public void localize(final int[] position) {
			source.localize(position);
		}

		@Override
		public LocalCursor copy() {
			return new LocalCursor(this);
		}

		@Override
		public LocalCursor copyCursor() {
			return copy();
		}
	}
}
