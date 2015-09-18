package net.imagej.ops.geometric3d;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.Geometric3D;
import net.imagej.ops.descriptor3d.CovarianceOf2ndMultiVariate3D;
import net.imagej.ops.descriptor3d.SecondMultiVariate3D;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BoolType;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Generic implementation of {@link SparenessFeature}. Use {@link FeatureSet} to
 * compile this {@link Op}.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 */
@Plugin(type = Op.class, name = Geometric3D.Spareness.NAME, label = "Geometric3D: Spareness", priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultSparenessFeature<B extends BooleanType<B>>
		extends
			AbstractFunctionOp<IterableRegion<B>, DoubleType>
		implements
			Geometric3DOp<IterableRegion<B>, DoubleType>,
			Geometric3D.Spareness {

	private FunctionOp<IterableRegion, DoubleType> mainElongation;
	
	private FunctionOp<IterableRegion, DoubleType> medianElongation;
	
	private FunctionOp<IterableRegion, CovarianceOf2ndMultiVariate3D> multivar;
	
	private FunctionOp<IterableRegion, DoubleType> volume;
	
	@Override
	public void initialize() {
		mainElongation = ops().function(DefaultMainElongationFeature.class, DoubleType.class, IterableRegion.class);
		medianElongation = ops().function(DefaultMedianElongationFeature.class, DoubleType.class, IterableRegion.class);
		multivar = ops().function(SecondMultiVariate3D.class, CovarianceOf2ndMultiVariate3D.class, IterableRegion.class);
		volume = ops().function(DefaultVolumeFeature.class, DoubleType.class, IterableRegion.class);
	}

	@Override
	public DoubleType compute(IterableRegion<B> input) {
		double r1 = Math.sqrt(5.0 * multivar.compute(input).getEigenvalue(0));
		double r2 = r1 / mainElongation.compute(input).get();
		double r3 = r2 / medianElongation.compute(input).get();

		double volumeEllipsoid = (4.18879 * r1 * r2 * r3);

		return new DoubleType(volume.compute(input).get() / volumeEllipsoid);
	}

}