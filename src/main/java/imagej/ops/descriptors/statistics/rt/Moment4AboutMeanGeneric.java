package imagej.ops.descriptors.statistics.rt;

import imagej.ops.Op;
import imagej.ops.descriptors.DescriptorService;
import imagej.ops.descriptors.misc.Area;
import imagej.ops.descriptors.statistics.Mean;
import imagej.ops.descriptors.statistics.Moment4AboutMean;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Moment4AboutMean}. Use
 * {@link DescriptorService} to compile this {@link Op}.
 * 
 * @author Christian Dietz
 * @author Andreas Graumann
 */
@Plugin(type = Op.class, name = Moment4AboutMean.NAME, label = Moment4AboutMean.LABEL, priority = Priority.VERY_HIGH_PRIORITY)
public class Moment4AboutMeanGeneric implements Moment4AboutMean {

	@Parameter(type = ItemIO.INPUT)
	private Iterable<? extends RealType<?>> irt;

	@Parameter(type = ItemIO.INPUT)
	private Mean mean;

	@Parameter(type = ItemIO.INPUT)
	private Area area;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;

	@Override
	public DoubleType getOutput() {
		return out;
	}

	@Override
	public void run() {
		final double meanVal = mean.getOutput().getRealDouble();

		double res = 0.0;
		for (final RealType<?> t : irt) {
			final double val = t.getRealDouble() - meanVal;
			res += val * val * val * val;
		}

		out = new DoubleType(res);
		out.div(area.getOutput());
	}

}
