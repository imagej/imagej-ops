package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Shear;
import net.imglib2.RandomAccessible;
import net.imglib2.view.TransformView;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.Shear.class, name = ViewOps.Shear.NAME)
public class DefaultShear<T> extends
		AbstractView<RandomAccessible<T>, TransformView<T>> implements
		Shear<RandomAccessible<T>, TransformView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private int shearDimension;

	@Parameter(type = ItemIO.INPUT)
	private int referenceDimension;

	@Override
	public TransformView<T> compute(RandomAccessible<T> input) {
		throw new UnsupportedOperationException();
	}

}
