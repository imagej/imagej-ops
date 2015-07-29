package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.ExtendMirrorSingle.class, name = Ops.View.ExtendMirrorSingle.NAME)
public class DefaultExtendMirrorSingle<T, F extends RandomAccessibleInterval<T>>
		extends AbstractFunctionOp<F, ExtendedRandomAccessibleInterval<T, F>>implements Ops.View.ExtendMirrorSingle {

	@Override
	public ExtendedRandomAccessibleInterval<T, F> compute(F input) {
		return Views.extendMirrorSingle(input);
	}
}
