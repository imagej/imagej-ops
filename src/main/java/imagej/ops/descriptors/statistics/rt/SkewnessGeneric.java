package imagej.ops.descriptors.statistics.rt;

import imagej.ops.Op;
import imagej.ops.descriptors.DescriptorService;
import imagej.ops.descriptors.statistics.Moment3AboutMean;
import imagej.ops.descriptors.statistics.Skewness;
import imagej.ops.descriptors.statistics.StdDev;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
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
public class SkewnessGeneric implements Skewness {

	@Parameter(type = ItemIO.INPUT)
	private Moment3AboutMean moment3;

	@Parameter(type = ItemIO.INPUT)
	private StdDev stdDev;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;

	@Override
	public DoubleType getOutput() {
		return out;
	}

	@Override
	public void run() {
		final double moment3 = this.moment3.getOutput().getRealDouble();
		final double std = this.stdDev.getOutput().getRealDouble();

		out = new DoubleType(0);
		if (std != 0) {
			out = new DoubleType(((moment3) / (std * std * std)));
		}

	}
}
