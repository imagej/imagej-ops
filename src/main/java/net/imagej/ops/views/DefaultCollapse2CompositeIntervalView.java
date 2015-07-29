package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.GenericComposite;

@Plugin(type = Ops.View.Collapse.class, name = Ops.View.Collapse.NAME)
public class DefaultCollapse2CompositeIntervalView<T>
		extends AbstractFunctionOp<RandomAccessibleInterval<T>, CompositeIntervalView<T, ? extends GenericComposite<T>>>
		implements Ops.View.Collapse {

	@Override
	public CompositeIntervalView<T, ? extends GenericComposite<T>> compute(RandomAccessibleInterval<T> input) {
		return Views.collapse(input);
	}

}