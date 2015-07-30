package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.View.PermuteCoordinates;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.PermuteCoordinates.class, name = Ops.View.PermuteCoordinates.NAME)
public class PermuteCoordinatesOfDimension<T> extends AbstractFunctionOp<RandomAccessibleInterval<T>, IntervalView<T>>
		implements PermuteCoordinates {

	@Parameter(type = ItemIO.INPUT)
	private int[] permutation;

	@Parameter(type = ItemIO.INPUT)
	private int d;

	@Override
	public IntervalView<T> compute(RandomAccessibleInterval<T> input) {
		return Views.permuteCoordinates(input, permutation, d);
	}

}
