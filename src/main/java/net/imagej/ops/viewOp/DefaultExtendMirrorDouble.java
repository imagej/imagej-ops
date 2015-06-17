package net.imagej.ops.viewOp;

import org.scijava.plugin.Plugin;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.ExtendMirrorDouble;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = ViewOps.ExtendMirrorDouble.class, name = ViewOps.ExtendMirrorDouble.NAME)
public class DefaultExtendMirrorDouble<T, F extends RandomAccessibleInterval<T>>
		extends AbstractView<F, ExtendedRandomAccessibleInterval<T, F>>
		implements ExtendMirrorDouble<F, ExtendedRandomAccessibleInterval<T, F>> {

	@Override
	public ExtendedRandomAccessibleInterval<T, F> compute(F input) {
		return Views.extendMirrorDouble(input);
	}

}
