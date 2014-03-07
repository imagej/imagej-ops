package imagej.ops.slicer;

import imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.Sampler;
import net.imglib2.iterator.IntervalIterator;

/**
 * 
 * @author dietzc
 */
public class SliceCursor extends IntervalIterator implements
		Cursor<RandomAccessibleInterval<?>> {

	private final long[] tmpPosition;
	private final OpService opService;
	private final RandomAccessibleInterval<?> src;
	private final long[] hyperSliceMax;

	public SliceCursor(final RandomAccessibleInterval<?> src,
			final OpService service, final Interval fixed,
			final Interval hyperSlice) {
		super(fixed);

		this.opService = service;
		this.src = src;

		this.hyperSliceMax = new long[hyperSlice.numDimensions()];
		hyperSlice.max(hyperSliceMax);

		this.tmpPosition = new long[fixed.numDimensions()];
	}

	@Override
	public RandomAccessibleInterval<?> get() {
		return null;
	}

	@Override
	public Sampler<RandomAccessibleInterval<?>> copy() {
		return null;
	}

	@Override
	public RandomAccessibleInterval<?> next() {
		fwd();
		localize(tmpPosition);
		return (RandomAccessibleInterval<?>) opService.run("hyperslicer", src,
				new FinalInterval(tmpPosition, hyperSliceMax));
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public Cursor<RandomAccessibleInterval<?>> copyCursor() {
		return null;
	}
}
