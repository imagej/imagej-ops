package net.imagej.ops.viewOp;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Rotate;
import net.imglib2.RandomAccessible;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.Rotate.class, name = ViewOps.Rotate.NAME)
public class RotateAroundAxis<T> extends
		AbstractView<RandomAccessible<T>, MixedTransformView<T>> implements
		Rotate<RandomAccessible<T>, MixedTransformView<T>> {

	@Parameter(type = ItemIO.INPUT)
	private int fromAxis;

	@Parameter(type = ItemIO.INPUT)
	private int toAxis;

	@Override
	public MixedTransformView<T> compute(RandomAccessible<T> input) {
		return Views.rotate(input, fromAxis, toAxis);
	}

}
