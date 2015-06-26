package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps.CollapseNumeric;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.NumericComposite;

public class DefaultCollapseNummeric2CompositeIntervalView<T extends NumericType<T>>
		extends
		AbstractView<RandomAccessibleInterval<T>, CompositeIntervalView<T, NumericComposite<T>>>
		implements
		CollapseNumeric<RandomAccessibleInterval<T>, CompositeIntervalView<T, NumericComposite<T>>> {

	@Override
	public CompositeIntervalView<T, NumericComposite<T>> compute(
			RandomAccessibleInterval<T> input) {
		return Views.collapseNumeric(input);
	}

}