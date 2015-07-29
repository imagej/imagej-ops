package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.NumericComposite;

@Plugin(type = Ops.View.NumericCollapse.class, name = Ops.View.NumericCollapse.NAME)
public class DefaultCollapseNumeric2CompositeIntervalView<T extends NumericType<T>>
		extends AbstractFunctionOp<RandomAccessibleInterval<T>, CompositeIntervalView<T, NumericComposite<T>>>
		implements Ops.View.NumericCollapse {

	@Override
	public CompositeIntervalView<T, NumericComposite<T>> compute(RandomAccessibleInterval<T> input) {
		return Views.collapseNumeric(input);
	}

}