package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.Extend.class, name = Ops.View.Extend.NAME)
public class DefaultExtend<T, F extends RandomAccessibleInterval<T>>
		extends AbstractFunctionOp<F, ExtendedRandomAccessibleInterval<T, F>>implements Ops.View.Extend {

	@Parameter(type = ItemIO.INPUT)
	private OutOfBoundsFactory<T, ? super F> factory;

	@Override
	public ExtendedRandomAccessibleInterval<T, F> compute(F input) {
		return Views.extend(input, factory);
	}

}
