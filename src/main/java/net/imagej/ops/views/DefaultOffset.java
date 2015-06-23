package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Offset;
import net.imglib2.RandomAccessible;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.Offset.class, name = ViewOps.Offset.NAME)
public class DefaultOffset<T> extends
		AbstractView<RandomAccessible<T>, MixedTransformView<T>> implements
		Offset<RandomAccessible<T>, MixedTransformView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private long[] offset;

	@Override
	public MixedTransformView<T> compute(RandomAccessible<T> input) {
		return Views.offset(input, offset);
	}

}
