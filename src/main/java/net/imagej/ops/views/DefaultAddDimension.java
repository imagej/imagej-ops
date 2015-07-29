package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessible;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.AddDimension.class, name = Ops.View.AddDimension.NAME)
public class DefaultAddDimension<T> extends AbstractFunctionOp<RandomAccessible<T>, MixedTransformView<T>>
		implements Ops.View.AddDimension {

	@Override
	public MixedTransformView<T> compute(RandomAccessible<T> input) {
		return Views.addDimension(input);
	}

}
