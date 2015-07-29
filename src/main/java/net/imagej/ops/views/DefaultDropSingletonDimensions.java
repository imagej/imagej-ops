package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.DropSingletonDimensions.class, name = Ops.View.DropSingletonDimensions.NAME)
public class DefaultDropSingletonDimensions<T>
		extends AbstractFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
		implements Ops.View.DropSingletonDimensions {

	@Override
	public RandomAccessibleInterval<T> compute(RandomAccessibleInterval<T> input) {
		return Views.dropSingletonDimensions(input);
	}

}
