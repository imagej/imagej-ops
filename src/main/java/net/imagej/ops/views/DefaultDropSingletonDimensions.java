package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.DropSingletonDimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.DropSingletonDimensions.class, name = ViewOps.DropSingletonDimensions.NAME)
public class DefaultDropSingletonDimensions<T> extends
		AbstractView<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
		implements
		DropSingletonDimensions<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> {

	@Override
	public RandomAccessibleInterval<T> compute(RandomAccessibleInterval<T> input) {
		return Views.dropSingletonDimensions(input);
	}

}
