package net.imagej.ops.views;

import net.imagej.ops.OpService;
import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.AddDimension;
import net.imagej.ops.view.ViewOps.View;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.IntervalView;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.AddDimension.class, name = ViewOps.AddDimension.NAME)
public class AddDimensionMinMax<T> extends
		AbstractView<RandomAccessibleInterval<T>, IntervalView<T>> implements
		AddDimension<RandomAccessibleInterval<T>, IntervalView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private long minOfNewDim;

	@Parameter(type = ItemIO.INPUT)
	private long maxOfNewDim;

	@Parameter
	private OpService ops;

	@Override
	public IntervalView<T> compute(RandomAccessibleInterval<T> input) {
		final int m = input.numDimensions();
		final long[] min = new long[m + 1];
		final long[] max = new long[m + 1];
		for (int d = 0; d < m; ++d) {
			min[d] = input.min(d);
			max[d] = input.max(d);
		}
		min[m] = minOfNewDim;
		max[m] = maxOfNewDim;
		return (IntervalView<T>) ops.run(View.class,
				ops.run(AddDimension.class, input), min, max);
	}

}
