package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessible;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeView;
import net.imglib2.view.composite.GenericComposite;

@Plugin(type = Ops.View.Collapse.class, name = Ops.View.Collapse.NAME)
public class DefaultCollapse2CompositeView<T>
		extends AbstractFunctionOp<RandomAccessible<T>, CompositeView<T, ? extends GenericComposite<T>>>
		implements Ops.View.Collapse {

	@Override
	public CompositeView<T, ? extends GenericComposite<T>> compute(RandomAccessible<T> input) {
		return Views.collapse(input);
	}

}
