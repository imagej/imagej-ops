package imagej.ops.slicer;

import imagej.ops.OpService;

import java.util.Arrays;
import java.util.Iterator;

import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.FlatIterationOrder;
import net.imglib2.Interval;
import net.imglib2.IterableInterval;
import net.imglib2.IterableRealInterval;
import net.imglib2.Positionable;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealPositionable;
import net.imglib2.util.Intervals;

public class SliceIterableInterval implements
		IterableInterval<RandomAccessibleInterval<?>> {

	private final Interval fixed;

	private final Interval hyperSlice;

	private final OpService opService;

	private final RandomAccessibleInterval<?> source;

	public SliceIterableInterval(final OpService opService,
			final RandomAccessibleInterval<?> source, final int[] axesOfInterest) {

		final long[] hyperSliceDimensions = new long[source.numDimensions()];
		final long[] dimensionsToIterate = new long[source.numDimensions()];

		Arrays.fill(hyperSliceDimensions, 1l);
		Arrays.fill(dimensionsToIterate, 1l);

		// determine axis to iterate
		int k = 0, l = 0;
		for (int i = 0; i < source.numDimensions(); i++) {
			boolean selected = false;
			for (int j = 0; j < axesOfInterest.length; j++) {

				if (axesOfInterest[j] == i) {
					selected = true;
				}

				if (!selected) {
					dimensionsToIterate[k] = source.dimension(i);
					k++;
				} else {
					hyperSliceDimensions[l] = source.dimension(i);
					l++;
				}
			}
		}

		this.source = source;
		this.fixed = new FinalInterval(dimensionsToIterate);
		this.hyperSlice = new FinalInterval(hyperSliceDimensions);
		this.opService = opService;
	}

	@Override
	public long size() {
		return Intervals.numElements(fixed);
	}

	@Override
	public RandomAccessibleInterval<?> firstElement() {
		return cursor().next();
	}

	@Override
	public Object iterationOrder() {
		return new FlatIterationOrder(fixed);
	}

	@Override
	public boolean equalIterationOrder(final IterableRealInterval<?> f) {
		return iterationOrder().equals(f);
	}

	@Override
	public double realMin(final int d) {
		return fixed.min(d);
	}

	@Override
	public void realMin(final double[] min) {
		fixed.realMin(min);
	}

	@Override
	public void realMin(final RealPositionable min) {
		fixed.realMin(min);
	}

	@Override
	public double realMax(final int d) {
		return fixed.realMax(d);
	}

	@Override
	public void realMax(final double[] max) {
		fixed.realMin(max);
	}

	@Override
	public void realMax(final RealPositionable max) {
		fixed.realMin(max);
	}

	@Override
	public int numDimensions() {
		return fixed.numDimensions();
	}

	@Override
	public Iterator<RandomAccessibleInterval<?>> iterator() {
		return cursor();
	}

	@Override
	public long min(final int d) {
		return fixed.min(d);
	}

	@Override
	public void min(final long[] min) {
		fixed.min(min);
	}

	@Override
	public void min(final Positionable min) {
		fixed.min(min);
	}

	@Override
	public long max(final int d) {
		return fixed.max(d);
	}

	@Override
	public void max(final long[] max) {
		fixed.max(max);
	}

	@Override
	public void max(final Positionable max) {
		fixed.max(max);
	}

	@Override
	public void dimensions(final long[] dimensions) {
		fixed.dimensions(dimensions);
	}

	@Override
	public long dimension(final int d) {
		return fixed.dimension(d);
	}

	@Override
	public Cursor<RandomAccessibleInterval<?>> cursor() {
		return new SliceCursor(source, opService, fixed, hyperSlice);
	}

	@Override
	public Cursor<RandomAccessibleInterval<?>> localizingCursor() {
		return cursor();
	}

}
