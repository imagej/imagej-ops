

package imagej.ops.descriptors.statistics.rt;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.descriptors.DescriptorService;
import imagej.ops.descriptors.statistics.Moment3AboutMean;
import imagej.ops.descriptors.statistics.Skewness;
import imagej.ops.descriptors.statistics.StdDev;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

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
@Plugin(type = Op.class, label = Skewness.LABEL, name = Skewness.NAME,
	priority = Priority.VERY_HIGH_PRIORITY)
public class SkewnessGeneric extends AbstractFunction<Object, RealType<?>>
	implements Skewness<Object, RealType<?>>
{

	@Parameter
	private Moment3AboutMean<Object, DoubleType> moment3;

	@Parameter
	private StdDev<Object, DoubleType> stdDev;

	@Override
	public RealType<?> compute(final Object input, RealType<?> output) {

		if (output == null) {
			output = new DoubleType();
			setOutput(output);
		}

		final double moment3 = this.moment3.getOutput().get();
		final double std = this.stdDev.getOutput().get();

		if (std != 0) output.setReal((moment3) / (std * std * std));
		else output.setReal(0.0);

		return output;
	}
}
