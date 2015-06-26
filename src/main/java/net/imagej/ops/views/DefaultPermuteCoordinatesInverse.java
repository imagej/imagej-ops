package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.PermuteCoordinatesInverse;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.IntervalView;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.PermuteCoordinatesInverse.class, name = PermuteCoordinatesInverse.NAME)
public class DefaultPermuteCoordinatesInverse<T> extends
		AbstractView<RandomAccessibleInterval<T>, IntervalView<T>> implements
		PermuteCoordinatesInverse<RandomAccessibleInterval<T>, IntervalView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private int[] permutation;

	@Parameter(type = ItemIO.INPUT)
	private int d;

	@Override
	public IntervalView<T> compute(RandomAccessibleInterval<T> input) {
		throw new UnsupportedOperationException();
	}

}
