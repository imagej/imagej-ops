package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.HyperSlice;
import net.imglib2.RandomAccessible;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.HyperSlice.class, name = ViewOps.HyperSlice.NAME)
public class DefaultHyperSlice<T> extends
		AbstractView<RandomAccessible<T>, MixedTransformView<T>> implements
		HyperSlice<RandomAccessible<T>, MixedTransformView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private int d;

	@Parameter(type = ItemIO.INPUT)
	private long pos;

	@Override
	public MixedTransformView<T> compute(RandomAccessible<T> input) {
		return Views.hyperSlice(input, d, pos);
	}

}
