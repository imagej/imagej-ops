package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.PermuteCoordinates.class, name = Ops.View.PermuteCoordinates.NAME)
public class DefaultPermuteCoordinates<T> extends AbstractFunctionOp<RandomAccessibleInterval<T>, IntervalView<T>>
		implements Ops.View.PermuteCoordinates {

	@Parameter(type = ItemIO.INPUT)
	private int[] permutation;

	@Override
	public IntervalView<T> compute(RandomAccessibleInterval<T> input) {
		return Views.permuteCoordinates(input, permutation);
	}

}
