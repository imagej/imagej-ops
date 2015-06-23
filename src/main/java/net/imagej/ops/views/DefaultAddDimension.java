package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.AddDimension;
import net.imglib2.RandomAccessible;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.AddDimension.class, name = ViewOps.AddDimension.NAME)
public class DefaultAddDimension<T> extends
		AbstractView<RandomAccessible<T>, MixedTransformView<T>> implements
		AddDimension<RandomAccessible<T>, MixedTransformView<T>> {

	@Override
	public MixedTransformView<T> compute(RandomAccessible<T> input) {
		return Views.addDimension(input);
	}

}
