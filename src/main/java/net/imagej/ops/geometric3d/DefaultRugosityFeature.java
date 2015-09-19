package net.imagej.ops.geometric3d;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Geometric3D.Rugosity}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = Geometric3D.Rugosity.NAME, label = "Geometric3D: Rugosity")
public class DefaultRugosityFeature<B extends BooleanType<B>> extends
		AbstractFunctionOp<IterableRegion<B>, DoubleType> implements
		Geometric3DOp<IterableRegion<B>, DoubleType>, Geometric3D.Rugosity {

	private FunctionOp<IterableRegion, DoubleType> surface;

	private FunctionOp<IterableRegion, DoubleType> convexHullSurface;

	@Override
	public void initialize() {
		surface = ops().function(DefaultSurfaceAreaFeature.class,
				DoubleType.class, IterableRegion.class);
		convexHullSurface = ops().function(
				DefaultConvexHullSurfaceAreaFeature.class, DoubleType.class,
				IterableRegion.class);
	}

	@Override
	public DoubleType compute(IterableRegion<B> input) {
		return new DoubleType(surface.compute(input).get()
				/ convexHullSurface.compute(input).get());
	}

}