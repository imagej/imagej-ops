package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.FlatIterable.class, name = Ops.View.FlatIterable.NAME)
public class DefaultFlatIterable<T> extends AbstractFunctionOp<RandomAccessibleInterval<T>, IterableInterval<T>>
		implements Ops.View.FlatIterable {

	@Override
	public IterableInterval<T> compute(RandomAccessibleInterval<T> input) {
		return Views.flatIterable(input);
	}

}
