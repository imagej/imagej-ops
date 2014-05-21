package imagej.ops.descriptors.statistics.rt;

import imagej.ops.Op;
import imagej.ops.descriptors.DescriptorService;
import imagej.ops.descriptors.misc.Area;
import imagej.ops.descriptors.statistics.AbstractFeature;
import imagej.ops.descriptors.statistics.Mean;
import imagej.ops.descriptors.statistics.Moment2AboutMean;
import imagej.ops.descriptors.statistics.Moment3AboutMean;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Moment3AboutMean}. Use
 * {@link DescriptorService} to compile this {@link Op}.
 * 
 * @author Christian Dietz
 * @author Andreas Graumann
 */
@Plugin(type = Op.class, name = Moment2AboutMean.NAME, label = Moment2AboutMean.LABEL, priority = Priority.VERY_HIGH_PRIORITY)
public class Moment3AboutMeanGeneric extends AbstractFeature implements
		Moment3AboutMean {

	@Parameter
	private Iterable<? extends RealType<?>> irt;

	@Parameter
	private Mean mean;

	@Parameter
	private Area area;

	@Override
	public double compute() {
		final double meanVal = mean.getFeature();

		double res = 0.0;
		for (final RealType<?> t : irt) {
			final double val = t.getRealDouble() - meanVal;
			res += val * val * val;
		}

		return (res / area.getFeature());
	}

}
