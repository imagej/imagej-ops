package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.AddDimension;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

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

	@Override
	public IntervalView<T> compute(RandomAccessibleInterval<T> input) {
		return Views.addDimension(input, minOfNewDim, maxOfNewDim);
	}

}
