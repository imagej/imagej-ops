package net.imagej.ops.geometric3d;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Generic implementation of {@link Geometric3D.Volume}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = Geometric3D.Volume.NAME, label = "Geometric3D: Volume", priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultVolumeFeature<B extends BooleanType<B>>
		extends
			AbstractFunctionOp<IterableRegion<B>, DoubleType>
		implements
			Geometric3DOp<IterableRegion<B>, DoubleType>,
			Geometric3D.Volume {

	@Override
	public DoubleType compute(IterableRegion<B> input) {
		return new DoubleType(input.size());
	}
}
