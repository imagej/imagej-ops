package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Collapse;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.GenericComposite;

import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.Collapse.class, name = ViewOps.Collapse.NAME)
public class DefaultCollapse2CompositeIntervalView<T>
		extends
		AbstractView<RandomAccessibleInterval<T>, CompositeIntervalView<T, ? extends GenericComposite<T>>>
		implements
		Collapse<RandomAccessibleInterval<T>, CompositeIntervalView<T, ? extends GenericComposite<T>>> {

	@Override
	public CompositeIntervalView<T, ? extends GenericComposite<T>> compute(
			RandomAccessibleInterval<T> input) {
		return Views.collapse(input);
	}

}