package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps.Offset;
import net.imglib2.Interval;
import net.imglib2.RandomAccessible;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public class OffsetInterval<T> extends
		AbstractView<RandomAccessible<T>, IntervalView<T>> implements
		Offset<RandomAccessible<T>, IntervalView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private Interval interval;

	@Override
	public IntervalView<T> compute(RandomAccessible<T> input) {
		return Views.offsetInterval(input, interval);
	}

}
