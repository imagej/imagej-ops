

package imagej.ops.descriptors.statistics.rt;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.descriptors.DescriptorService;
import imagej.ops.descriptors.statistics.Skewness;
import imagej.ops.descriptors.statistics.StdDev;
import imagej.ops.descriptors.statistics.Variance;
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
@Plugin(type = Op.class, label = StdDev.LABEL, name = StdDev.NAME,
	priority = Priority.HIGH_PRIORITY)
public class StdDevGeneric extends AbstractFunction<Object, DoubleType>
	implements StdDev<Object, DoubleType>
{

	@Parameter
	private Variance<Object, DoubleType> variance;

	@Override
	public DoubleType compute(final Object input, DoubleType output) {

		if (output == null) {
			output = new DoubleType();
			setOutput(output);
		}

		output.set(Math.sqrt(variance.getOutput().get()));

		return output;
	}
}
