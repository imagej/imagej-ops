package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;

@Plugin(type = Ops.View.Stack.class, name = Ops.View.Stack.NAME)
public class DefaultStack<T> extends AbstractFunctionOp<RandomAccessibleInterval<T>[], RandomAccessibleInterval<T>>
		implements Ops.View.Stack {

	@Override
	public RandomAccessibleInterval<T> compute(RandomAccessibleInterval<T>[] input) {
		throw new UnsupportedOperationException();
	}

}
