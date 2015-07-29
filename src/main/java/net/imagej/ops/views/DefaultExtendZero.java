package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.ExtendZero.class, name = Ops.View.ExtendZero.NAME)
public class DefaultExtendZero<T extends NumericType<T>, F extends RandomAccessibleInterval<T>>
		extends AbstractFunctionOp<F, ExtendedRandomAccessibleInterval<T, F>>implements Ops.View.ExtendZero {

	@Override
	public ExtendedRandomAccessibleInterval<T, F> compute(F input) {
		return Views.extendZero(input);
	}

}
