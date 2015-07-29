package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessible;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.Interval.class, name = Ops.View.Interval.NAME)
public class IntervalMinMax<T> extends AbstractFunctionOp<RandomAccessible<T>, IntervalView<T>>
		implements Ops.View.Interval {

	@Parameter(type = ItemIO.INPUT)
	private long[] min;

	@Parameter(type = ItemIO.INPUT)
	private long[] max;

	@Override
	public IntervalView<T> compute(RandomAccessible<T> input) {
		return Views.interval(input, min, max);
	}

}
