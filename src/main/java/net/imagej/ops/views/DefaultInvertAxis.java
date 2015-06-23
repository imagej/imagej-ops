package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.InvertAxis;
import net.imglib2.RandomAccessible;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.InvertAxis.class, name = ViewOps.InvertAxis.NAME)
public class DefaultInvertAxis<T> extends
		AbstractView<RandomAccessible<T>, MixedTransformView<T>> implements
		InvertAxis<RandomAccessible<T>, MixedTransformView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private int d;

	@Override
	public MixedTransformView<T> compute(RandomAccessible<T> input) {
		return Views.invertAxis(input, d);
	}

}
