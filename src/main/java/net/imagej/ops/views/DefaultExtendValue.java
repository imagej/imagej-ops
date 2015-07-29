package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.ExtendValue.class, name = Ops.View.ExtendValue.NAME)
public class DefaultExtendValue<T extends Type<T>, F extends RandomAccessibleInterval<T>>
		extends AbstractFunctionOp<F, ExtendedRandomAccessibleInterval<T, F>>implements Ops.View.ExtendValue {

	@Parameter(type = ItemIO.INPUT)
	private T value;

	@Override
	public ExtendedRandomAccessibleInterval<T, F> compute(F input) {
		return Views.extendValue(input, value);
	}

}
