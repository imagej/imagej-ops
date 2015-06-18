package net.imagej.ops.views;


import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Interpolate;
import net.imglib2.EuclideanSpace;
import net.imglib2.RealRandomAccessible;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.type.Type;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = ViewOps.Interpolate.class, name = ViewOps.Interpolate.NAME)
public class DefaultInterpolate<I extends EuclideanSpace, T extends Type<T>> extends
		AbstractView<I, RealRandomAccessible<T>> implements
		Interpolate<I, RealRandomAccessible<T>> {

	@Parameter(type=ItemIO.INPUT)
	private InterpolatorFactory< T, I > factory;
	
	@Override
	public RealRandomAccessible<T> compute(I input) {
		return Views.interpolate(input, factory);
	}

}
