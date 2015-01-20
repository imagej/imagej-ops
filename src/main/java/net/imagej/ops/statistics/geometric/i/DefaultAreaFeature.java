package net.imagej.ops.statistics.geometric.i;

import java.util.Iterator;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.statistics.geometric.GeometricStatOps.Area;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * @author Daniel Seebacher (University of Konstanz)
 */
@Plugin(type = Op.class, name = Area.NAME, label = Area.NAME, priority=Priority.VERY_LOW_PRIORITY)
public class DefaultAreaFeature extends AbstractOutputFunction<Iterable<?>, RealType<?>>
		implements AreaFeature {

	@Override
	public double getFeatureValue() {
		return getOutput().getRealDouble();
	}

	@Override
	public RealType<?> createOutput(Iterable<?> input) {
		return new LongType();
	}

	@Override
	protected RealType<?> safeCompute(Iterable<?> input, RealType<?> output) {

		long sum = 0;

		Iterator<?> iterator = input.iterator();
		while (iterator.hasNext()) {
			iterator.next();
			++sum;
		}

		output.setReal(sum);
		return output;
	}
}
