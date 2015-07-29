package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessible;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.InvertAxis.class, name = Ops.View.InvertAxis.NAME)
public class DefaultInvertAxis<T> extends AbstractFunctionOp<RandomAccessible<T>, MixedTransformView<T>>
		implements Ops.View.InvertAxis {

	@Parameter(type = ItemIO.INPUT)
	private int d;

	@Override
	public MixedTransformView<T> compute(RandomAccessible<T> input) {
		return Views.invertAxis(input, d);
	}

}
