package net.imagej.ops.viewOp;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.ExtendZero;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.ExtendZero.class, name = ViewOps.ExtendZero.NAME)
public class DefaultExtendZero<T extends NumericType<T>, F extends RandomAccessibleInterval<T>>
		extends AbstractView<F, ExtendedRandomAccessibleInterval<T, F>>
		implements ExtendZero<F, ExtendedRandomAccessibleInterval<T, F>> {

	@Override
	public ExtendedRandomAccessibleInterval<T, F> compute(F input) {
		return Views.extendZero(input);
	}

}
