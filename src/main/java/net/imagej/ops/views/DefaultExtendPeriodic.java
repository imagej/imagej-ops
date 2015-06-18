package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.ExtendPeriodic;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = ViewOps.ExtendPeriodic.class, name = ViewOps.ExtendPeriodic.NAME)
public class DefaultExtendPeriodic<T, F extends RandomAccessibleInterval<T>>
		extends AbstractView<F, ExtendedRandomAccessibleInterval<T, F>>
		implements ExtendPeriodic<F, ExtendedRandomAccessibleInterval<T, F>> {

	@Override
	public ExtendedRandomAccessibleInterval<T, F> compute(F input) {
		return Views.extendPeriodic(input);
	}
}
