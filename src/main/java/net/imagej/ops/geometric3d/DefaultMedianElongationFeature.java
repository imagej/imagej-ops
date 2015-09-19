package net.imagej.ops.geometric3d;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D;
import net.imagej.ops.descriptor3d.CovarianceOf2ndMultiVariate3D;
import net.imagej.ops.descriptor3d.SecondMultiVariate3D;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Geometric3D.MedianElongation}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = Geometric3D.MedianElongation.NAME, label = "Geometric3D: MedianElongation", priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultMedianElongationFeature<B extends BooleanType<B>>
		extends
			AbstractFunctionOp<IterableRegion<B>, DoubleType>
		implements
			Geometric3DOp<IterableRegion<B>, DoubleType>,
			Geometric3D.MedianElongation {

	private FunctionOp<IterableRegion, CovarianceOf2ndMultiVariate3D> multivar;
	
	@Override
	public void initialize() {
		multivar = ops().function(SecondMultiVariate3D.class, CovarianceOf2ndMultiVariate3D.class, IterableRegion.class);
	}

	@Override
	public DoubleType compute(IterableRegion<B> input) {
		CovarianceOf2ndMultiVariate3D compute = multivar.compute(input);
		return new DoubleType(
				Math.sqrt(compute.getEigenvalue(1) / compute.getEigenvalue(2)));
	}

}