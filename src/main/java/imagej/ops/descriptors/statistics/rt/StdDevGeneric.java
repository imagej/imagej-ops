package imagej.ops.descriptors.statistics.rt;

import imagej.ops.Op;
import imagej.ops.descriptors.DescriptorService;
import imagej.ops.descriptors.statistics.Skewness;
import imagej.ops.descriptors.statistics.StdDev;
import imagej.ops.descriptors.statistics.Variance;
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
@Plugin(type = Op.class, label = StdDev.LABEL, name = StdDev.NAME, priority = Priority.HIGH_PRIORITY)
public class StdDevGeneric implements StdDev {

	@Parameter(type = ItemIO.INPUT)
	private Variance variance;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;

	@Override
	public DoubleType getOutput() {
		return out;
	}

	@Override
	public void run() {
		out = new DoubleType(Math.sqrt(variance.getOutput().getRealDouble()));
	}
}
