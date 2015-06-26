package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.CollapseReal;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.RealComposite;

import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.CollapseReal.class, name = ViewOps.CollapseReal.NAME)
public class DefaultRealCollapse2CompositeIntervalView<T extends RealType<T>>
		extends
		AbstractView<RandomAccessibleInterval<T>, CompositeIntervalView<T, RealComposite<T>>>
		implements
		CollapseReal<RandomAccessibleInterval<T>, CompositeIntervalView<T, RealComposite<T>>> {

	@Override
	public CompositeIntervalView<T, RealComposite<T>> compute(
			RandomAccessibleInterval<T> input) {
		return Views.collapseReal(input);
	}

}
