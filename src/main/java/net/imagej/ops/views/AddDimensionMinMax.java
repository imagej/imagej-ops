package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.AddDimension.class, name = Ops.View.AddDimension.NAME)
public class AddDimensionMinMax<T> extends AbstractFunctionOp<RandomAccessibleInterval<T>, IntervalView<T>>
		implements Ops.View.AddDimension {

	@Parameter(type = ItemIO.INPUT)
	private long minOfNewDim;

	@Parameter(type = ItemIO.INPUT)
	private long maxOfNewDim;

	@Override
	public IntervalView<T> compute(RandomAccessibleInterval<T> input) {
		return Views.addDimension(input, minOfNewDim, maxOfNewDim);
	}

}
