package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Extend;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = ViewOps.Extend.class, name = ViewOps.Extend.NAME)
public class DefaultExtend<T, F extends RandomAccessibleInterval<T>> extends
		AbstractView<F, ExtendedRandomAccessibleInterval<T, F>> implements
		Extend<F, ExtendedRandomAccessibleInterval<T, F>> {

	@Parameter(type = ItemIO.INPUT)
	private OutOfBoundsFactory<T, ? super F> factory;

	@Override
	public ExtendedRandomAccessibleInterval<T, F> compute(F input) {
		return Views.extend(input, factory);
	}

}
