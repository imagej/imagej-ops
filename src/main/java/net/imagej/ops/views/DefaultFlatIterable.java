package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.FlatIterable;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.FlatIterable.class, name = ViewOps.FlatIterable.NAME)
public class DefaultFlatIterable<T> extends
		AbstractView<RandomAccessibleInterval<T>, IterableInterval<T>>
		implements
		FlatIterable<RandomAccessibleInterval<T>, IterableInterval<T>> {

	@Override
	public IterableInterval<T> compute(RandomAccessibleInterval<T> input) {
		return Views.flatIterable(input);
	}

}
