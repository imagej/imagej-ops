package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Collapse;
import net.imglib2.RandomAccessible;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeView;
import net.imglib2.view.composite.GenericComposite;

import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.Collapse.class, name = ViewOps.Collapse.NAME)
public class DefaultCollapse2CompositeView<T>
		extends
		AbstractView<RandomAccessible<T>, CompositeView<T, ? extends GenericComposite<T>>>
		implements
		Collapse<RandomAccessible<T>, CompositeView<T, ? extends GenericComposite<T>>> {

	@Override
	public CompositeView<T, ? extends GenericComposite<T>> compute(
			RandomAccessible<T> input) {
		return Views.collapse(input);
	}

}
