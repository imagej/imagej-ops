package net.imagej.ops.views;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Ops;
import net.imglib2.RealRandomAccessible;
import net.imglib2.type.Type;
import net.imglib2.view.RandomAccessibleOnRealRandomAccessible;
import net.imglib2.view.Views;

@Plugin(type = Ops.View.Raster.class, name = Ops.View.Raster.NAME)
public class DefaultRaster<T extends Type<T>>
		extends AbstractFunctionOp<RealRandomAccessible<T>, RandomAccessibleOnRealRandomAccessible<T>>
		implements Ops.View.Raster {

	@Override
	public RandomAccessibleOnRealRandomAccessible<T> compute(RealRandomAccessible<T> input) {
		return Views.raster(input);
	}

}
