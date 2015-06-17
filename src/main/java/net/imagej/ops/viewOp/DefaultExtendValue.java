package net.imagej.ops.viewOp;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.ExtendValue;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = ViewOps.ExtendValue.class, name = ViewOps.ExtendValue.NAME)
public class DefaultExtendValue<T extends Type<T>, F extends RandomAccessibleInterval<T>>
		extends AbstractView<F, ExtendedRandomAccessibleInterval<T, F>>
		implements ExtendValue<F, ExtendedRandomAccessibleInterval<T, F>> {

	@Parameter(type = ItemIO.INPUT)
	private T value;

	@Override
	public ExtendedRandomAccessibleInterval<T, F> compute(F input) {
		return Views.extendValue(input, value);
	}

}
