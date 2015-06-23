package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Permute;
import net.imglib2.RandomAccessible;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.Permute.class, name = ViewOps.Permute.NAME)
public class DefaultPermute<T> extends
		AbstractView<RandomAccessible<T>, MixedTransformView<T>> implements
		Permute<RandomAccessible<T>, MixedTransformView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private int fromAxis;

	@Parameter(type = ItemIO.INPUT)
	private int toAxis;

	@Override
	public MixedTransformView<T> compute(RandomAccessible<T> input) {
		return Views.permute(input, fromAxis, toAxis);
	}

}
