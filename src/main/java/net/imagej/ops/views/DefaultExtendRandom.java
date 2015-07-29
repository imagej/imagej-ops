package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.ExtendRandom.class, name = Ops.View.ExtendRandom.NAME)
public class DefaultExtendRandom<T extends RealType<T>, F extends RandomAccessibleInterval<T>>
		extends AbstractFunctionOp<F, ExtendedRandomAccessibleInterval<T, F>>implements Ops.View.ExtendRandom {

	@Parameter(type = ItemIO.INPUT)
	private double min;

	@Parameter(type = ItemIO.INPUT)
	private double max;

	@Override
	public ExtendedRandomAccessibleInterval<T, F> compute(F input) {
		return Views.extendRandom(input, min, max);
	}
}
