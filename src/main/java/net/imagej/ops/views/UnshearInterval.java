package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.Interval;
import net.imglib2.RandomAccessible;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.UnShear.class, name = Ops.View.UnShear.NAME)
public class UnshearInterval<T> extends AbstractFunctionOp<RandomAccessible<T>, IntervalView<T>>
		implements Ops.View.UnShear {

	@Parameter(type = ItemIO.INPUT)
	private Interval interval;

	@Parameter(type = ItemIO.INPUT)
	private int shearDimension;

	@Parameter(type = ItemIO.INPUT)
	private int referenceDimension;

	@Override
	public IntervalView<T> compute(RandomAccessible<T> input) {
		return Views.unshear(input, interval, shearDimension, referenceDimension);
	}
}
