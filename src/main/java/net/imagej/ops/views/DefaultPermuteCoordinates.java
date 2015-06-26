package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.PermuteCoordinates;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.IntervalView;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.PermuteCoordinates.class, name = ViewOps.PermuteCoordinates.NAME)
public class DefaultPermuteCoordinates<T> extends
		AbstractView<RandomAccessibleInterval<T>, IntervalView<T>> implements
		PermuteCoordinates<RandomAccessibleInterval<T>, IntervalView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private int[] permutation;

	@Override
	public IntervalView<T> compute(RandomAccessibleInterval<T> input) {
		throw new UnsupportedOperationException();
	}

}
