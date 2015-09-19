package net.imagej.ops.geometric3d;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Geometric3D.Compactness}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = Geometric3D.Compactness.NAME, label = "Geometric3D: Compactness", priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultCompactnessFeature<B extends BooleanType<B>>
		extends
			AbstractFunctionOp<IterableRegion<B>, DoubleType>
		implements
			Geometric3DOp<IterableRegion<B>, DoubleType>,
			Geometric3D.Volume {

	private FunctionOp<IterableRegion, DoubleType> surfacePixel;
	
	private FunctionOp<IterableRegion, DoubleType> volume;
	
	@Override
	public void initialize() {
		surfacePixel = ops().function(DefaultSurfacePixelFeature.class, DoubleType.class, IterableRegion.class);
		volume = ops().function(DefaultVolumeFeature.class, DoubleType.class, IterableRegion.class);
	}
	
	@Override
	public DoubleType compute(IterableRegion<B> input) {
		double s3 = Math.pow(surfacePixel.compute(input).get(), 3);
		double v2 = Math.pow(volume.compute(input).get(), 2);

		return new DoubleType((v2 * 36.0 * Math.PI) / s3);
	}

}
