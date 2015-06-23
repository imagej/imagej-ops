package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.ZeroMin;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

@Plugin(type = ViewOps.ZeroMin.class, name = ViewOps.ZeroMin.NAME)
public class DefaultZeroMin<T> extends
		AbstractView<RandomAccessibleInterval<T>, IntervalView<T>> implements
		ZeroMin<RandomAccessibleInterval<T>, IntervalView<T>> {

	@Override
	public IntervalView<T> compute(RandomAccessibleInterval<T> input) {
		return Views.zeroMin(input);
	}

}
