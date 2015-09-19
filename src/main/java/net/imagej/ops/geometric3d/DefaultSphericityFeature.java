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
 * Generic implementation of {@link Geometric3D.Sphericity}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = Geometric3D.Sphericity.NAME, label = "Geometric3D: Sphericity", priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultSphericityFeature<B extends BooleanType<B>> extends
		AbstractFunctionOp<IterableRegion<B>, DoubleType> implements
		Geometric3DOp<IterableRegion<B>, DoubleType>, Geometric3D.Sphericity {

	private FunctionOp<IterableRegion, DoubleType> compactness;

	@Override
	public void initialize() {
		compactness = ops().function(DefaultCompactnessFeature.class,
				DoubleType.class, IterableRegion.class);
	}

	@Override
	public DoubleType compute(IterableRegion<B> input) {
		return new DoubleType(Math.pow(compactness.compute(input).get(),
				(1 / 3d)));
	}

}