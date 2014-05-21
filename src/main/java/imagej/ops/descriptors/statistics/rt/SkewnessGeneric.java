package imagej.ops.descriptors.statistics.rt;

import imagej.ops.Op;
import imagej.ops.descriptors.DescriptorService;
import imagej.ops.descriptors.statistics.AbstractFeature;
import imagej.ops.descriptors.statistics.Moment3AboutMean;
import imagej.ops.descriptors.statistics.Skewness;
import imagej.ops.descriptors.statistics.StdDev;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Skewness}. Use {@link DescriptorService} to
 * compile this {@link Op}.
 * 
 * @author Christian Dietz
 * @author Andreas Graumann
 */
@Plugin(type = Op.class, label = Skewness.LABEL, name = Skewness.NAME, priority = Priority.VERY_HIGH_PRIORITY)
public class SkewnessGeneric extends AbstractFeature implements Skewness {

	@Parameter
	private Moment3AboutMean moment3;

	@Parameter
	private StdDev stdDev;

	@Override
	public double compute() {
		final double moment3 = this.moment3.getFeature();
		final double std = this.stdDev.getFeature();

		if (std != 0) {
			return ((moment3) / (std * std * std));
		}

		return 0;
	}
}
