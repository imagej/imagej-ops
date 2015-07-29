package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.ExtendPeriodic.class, name = Ops.View.ExtendPeriodic.NAME)
public class DefaultExtendPeriodic<T, F extends RandomAccessibleInterval<T>>
		extends AbstractFunctionOp<F, ExtendedRandomAccessibleInterval<T, F>>implements Ops.View.ExtendPeriodic {

	@Override
	public ExtendedRandomAccessibleInterval<T, F> compute(F input) {
		return Views.extendPeriodic(input);
	}
}
