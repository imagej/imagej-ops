package net.imagej.ops.views;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Stack;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.Stack.class, name = ViewOps.Stack.NAME)
public class DefaultStack<T>
		extends
		AbstractView<RandomAccessibleInterval<T>[], RandomAccessibleInterval<T>>
		implements
		Stack<RandomAccessibleInterval<T>[], RandomAccessibleInterval<T>> {

	@Override
	public RandomAccessibleInterval<T> compute(
			RandomAccessibleInterval<T>[] input) {
		throw new UnsupportedOperationException();
	}

}
