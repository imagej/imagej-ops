package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.ZeroMin.class, name = Ops.View.ZeroMin.NAME)
public class DefaultZeroMin<T> extends AbstractFunctionOp<RandomAccessibleInterval<T>, IntervalView<T>>
		implements Ops.View.IsZeroMin {

	@Override
	public IntervalView<T> compute(RandomAccessibleInterval<T> input) {
		return Views.zeroMin(input);
	}

}
