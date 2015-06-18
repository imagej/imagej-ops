package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.view.ViewOps;
import net.imagej.ops.view.ViewOps.Raster;
import net.imglib2.RealRandomAccessible;
import net.imglib2.type.Type;
import net.imglib2.view.RandomAccessibleOnRealRandomAccessible;
import net.imglib2.view.Views;

@Plugin(type = ViewOps.Raster.class, name = ViewOps.Raster.NAME)
public class DefaultRaster<T extends Type<T>>
		extends
		AbstractView<RealRandomAccessible<T>, RandomAccessibleOnRealRandomAccessible<T>>
		implements
		Raster<RealRandomAccessible<T>, RandomAccessibleOnRealRandomAccessible<T>> {

	@Override
	public RandomAccessibleOnRealRandomAccessible<T> compute(
			RealRandomAccessible<T> input) {
		return Views.raster(input);
	}

}
