package net.imagej.ops.viewOp;

import net.imagej.ops.Op;
import net.imagej.ops.ViewOps.View;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = View.NAME)
public class Interval<I extends Type<I>> extends AbstractDefaultView<I> {

	@Override
	public RandomAccessibleInterval<?> compute(RandomAccessibleInterval<I> input) {
		return Views.interval(input, input);
	}

}
