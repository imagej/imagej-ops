package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessible;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.Offset.class, name = Ops.View.Offset.NAME)
public class DefaultOffset<T> extends AbstractFunctionOp<RandomAccessible<T>, MixedTransformView<T>>
		implements Ops.View.Offset {

	@Parameter(type = ItemIO.INPUT)
	private long[] offset;

	@Override
	public MixedTransformView<T> compute(RandomAccessible<T> input) {
		return Views.offset(input, offset);
	}

}
