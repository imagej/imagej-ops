package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.ExtendMirrorSingle;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = ViewOps.ExtendMirrorSingle.class, name = ViewOps.ExtendMirrorSingle.NAME)
public class DefaultExtendMirrorSingle<T, F extends RandomAccessibleInterval<T>>
		extends AbstractView<F, ExtendedRandomAccessibleInterval<T, F>>
		implements
		ExtendMirrorSingle<F, ExtendedRandomAccessibleInterval<T, F>> {

	@Override
	public ExtendedRandomAccessibleInterval<T, F> compute(F input) {
		return Views.extendMirrorSingle(input);
	}
}
