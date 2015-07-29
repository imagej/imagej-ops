package net.imagej.ops.views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.EuclideanSpace;
import net.imglib2.RealRandomAccessible;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.type.Type;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.Interpolate.class, name = Ops.View.Interpolate.NAME)
public class DefaultInterpolate<I extends EuclideanSpace, T extends Type<T>>
		extends AbstractFunctionOp<I, RealRandomAccessible<T>>implements Ops.View.Interpolate {

	@Parameter(type = ItemIO.INPUT)
	private InterpolatorFactory<T, I> factory;

	@Override
	public RealRandomAccessible<T> compute(I input) {
		return Views.interpolate(input, factory);
	}

}
