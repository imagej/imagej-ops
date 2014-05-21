package imagej.ops.descriptors.statistics.rt;

import imagej.ops.Op;
import imagej.ops.descriptors.DescriptorService;
import imagej.ops.descriptors.statistics.AbstractFeature;
import imagej.ops.descriptors.statistics.Skewness;
import imagej.ops.descriptors.statistics.StdDev;
import imagej.ops.descriptors.statistics.Variance;

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
@Plugin(type = Op.class, label = StdDev.LABEL, name = StdDev.NAME, priority = Priority.HIGH_PRIORITY)
public class StdDevGeneric extends AbstractFeature implements StdDev {

	@Parameter
	private Variance variance;

	@Override
	public double compute() {
		return Math.sqrt(variance.getFeature());
	}
}
