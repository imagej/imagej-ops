package net.imagej.ops.geometric3d;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D;
import net.imagej.ops.descriptor3d.DefaultFacets;
import net.imagej.ops.descriptor3d.QuickHull3DFromMC;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Generic implementation of {@link ConvexHullSurfacePixelFeature}. Use
 * {@link FeatureSet} to compile this {@link Op}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = Geometric3D.ConvexHullSurfacePixel.NAME, label = "Geometric3D: ConvexHullSurfacePixel", priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultConvexHullSurfacePixelFeature<B extends BooleanType<B>>
		extends
			AbstractFunctionOp<IterableRegion<B>, DoubleType>
		implements
			Geometric3DOp<IterableRegion<B>, DoubleType>,
			Geometric3D.ConvexHullSurfacePixel {

	private FunctionOp<IterableRegion, DefaultFacets> convexHull;

	@Override
	public void initialize() {
		convexHull = ops().function(QuickHull3DFromMC.class,
				DefaultFacets.class, IterableRegion.class);
	}

	@Override
	public DoubleType compute(IterableRegion<B> input) {
		return new DoubleType(convexHull.compute(input).getPoints().size());
	}

}