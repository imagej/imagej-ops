package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.RealComposite;

@Plugin(type = Ops.View.RealCollapse.class, name = Ops.View.RealCollapse.NAME)
public class DefaultCollapseReal2CompositeIntervalView<T extends RealType<T>>
		extends AbstractFunctionOp<RandomAccessibleInterval<T>, CompositeIntervalView<T, RealComposite<T>>>
		implements Ops.View.RealCollapse {

	@Override
	public CompositeIntervalView<T, RealComposite<T>> compute(RandomAccessibleInterval<T> input) {
		return Views.collapseReal(input);
	}

}
