package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessible;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.Rotate.class, name = Ops.View.Rotate.NAME)
public class RotateAroundAxis<T> extends AbstractFunctionOp<RandomAccessible<T>, MixedTransformView<T>>
		implements Ops.View.Rotate {

	@Parameter(type = ItemIO.INPUT)
	private int fromAxis;

	@Parameter(type = ItemIO.INPUT)
	private int toAxis;

	@Override
	public MixedTransformView<T> compute(RandomAccessible<T> input) {
		return Views.rotate(input, fromAxis, toAxis);
	}

}
