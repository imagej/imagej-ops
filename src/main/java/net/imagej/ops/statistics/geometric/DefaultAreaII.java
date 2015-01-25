package net.imagej.ops.statistics.geometric;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.statistics.geometric.GeometricStatOps.Area;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Op.class, name = Area.NAME, label = Area.NAME)
public class DefaultAreaII<O extends RealType<O>> extends
		AbstractOutputFunction<IterableInterval<?>, O> implements
		AreaFeature<O>, AreaII<O> {

	@Override
	protected O safeCompute(IterableInterval<?> input, O output) {
		output.setReal((double) input.size());
		return output;
	}

	@Override
	public O createOutput(IterableInterval<?> input) {
		return (O) new DoubleType();
	}

}
