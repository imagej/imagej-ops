package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.UnShear;
import net.imglib2.RandomAccessible;
import net.imglib2.view.TransformView;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.UnShear.class, name = ViewOps.UnShear.NAME)
public class DefaultUnshear<T> extends
		AbstractView<RandomAccessible<T>, TransformView<T>> implements
		UnShear<RandomAccessible<T>, TransformView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private int shearDimension;

	@Parameter(type = ItemIO.INPUT)
	private int referenceDimension;

	@Override
	public TransformView<T> compute(RandomAccessible<T> input) {
		throw new UnsupportedOperationException();
	}

}
