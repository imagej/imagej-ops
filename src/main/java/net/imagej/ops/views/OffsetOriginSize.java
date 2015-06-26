package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps.Offset;
import net.imglib2.RandomAccessible;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public class OffsetOriginSize<T> extends
		AbstractView<RandomAccessible<T>, IntervalView<T>> implements
		Offset<RandomAccessible<T>, IntervalView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private long[] origin;

	@Parameter(type = ItemIO.INPUT)
	private long[] dimension;

	@Override
	public IntervalView<T> compute(RandomAccessible<T> input) {
		return Views.offsetInterval(input, origin, dimension);
	}

}
