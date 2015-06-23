package net.imagej.ops.views;

import net.imagej.ops.OpService;
import net.imagej.ops.view.ViewOps.Offset;
import net.imagej.ops.view.ViewOps.View;
import net.imglib2.Interval;
import net.imglib2.RandomAccessible;
import net.imglib2.view.IntervalView;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public class OffsetInterval<T> extends
		AbstractView<RandomAccessible<T>, IntervalView<T>> implements
		Offset<RandomAccessible<T>, IntervalView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private Interval interval;

	@Parameter
	private OpService ops;

	@Override
	public IntervalView<T> compute(RandomAccessible<T> input) {
		final int n = input.numDimensions();
		final long[] offset = new long[n];
		final long[] min = new long[n];
		final long[] max = new long[n];
		interval.min(offset);
		interval.max(max);
		for (int d = 0; d < n; ++d)
			max[d] -= offset[d];

		return (IntervalView<T>) ops.run(View.class,
				ops.run(Offset.class, input, offset), min, max);
	}

}
