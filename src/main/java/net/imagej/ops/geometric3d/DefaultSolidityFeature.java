package net.imagej.ops.geometric3d;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D;
import net.imagej.ops.Ops.Geometric3D.ConvexHullVolume;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Generic implementation of {@link SolidityFeature}. Use {@link FeatureSet} to
 * compile this {@link Op}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = Geometric3D.Solidity.NAME, label = "Geometric3D: Solidity")
public class DefaultSolidityFeature<B extends BooleanType<B>>
		extends
			AbstractFunctionOp<IterableRegion<B>, DoubleType>
		implements
			Geometric3DOp<IterableRegion<B>, DoubleType>,
			Geometric3D.Solidity {

	private FunctionOp<IterableRegion, DoubleType> volume;
	
	private FunctionOp<IterableRegion, DoubleType> convexHullVolume;
	
	@Override
	public void initialize() {
		volume = ops().function(DefaultVolumeFeature.class, DoubleType.class, IterableRegion.class);
		convexHullVolume = ops().function(DefaultConvexHullVolumeFeature.class, DoubleType.class, IterableRegion.class);
	}

	@Override
	public DoubleType compute(IterableRegion<B> input) {
		return new DoubleType(
				volume.compute(input).get() / convexHullVolume.compute(input).get());
	}

}