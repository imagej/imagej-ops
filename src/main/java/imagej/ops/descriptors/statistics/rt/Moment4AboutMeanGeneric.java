
package imagej.ops.descriptors.statistics.rt;

import imagej.ops.Op;
import imagej.ops.descriptors.DescriptorService;
import imagej.ops.descriptors.misc.Area;
import imagej.ops.descriptors.statistics.Mean;
import imagej.ops.descriptors.statistics.Moment4AboutMean;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

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
@Plugin(type = Op.class, name = Moment4AboutMean.NAME,
	label = Moment4AboutMean.LABEL, priority = Priority.VERY_HIGH_PRIORITY)
public class Moment4AboutMeanGeneric extends AbstractFunctionIRT implements
	Moment4AboutMean<Iterable<RealType<?>>, RealType<?>>
{

	@Parameter
	private Mean<Iterable<RealType<?>>, DoubleType> mean;

	@Parameter
	private Area<Iterable<?>, DoubleType> area;

	@Override
	public RealType<?> compute(final Iterable<RealType<?>> input,
		RealType<?> output)
	{
		if (output == null) {
			output = new DoubleType();
			setOutput(output);
		}

		final double meanVal = mean.getOutput().get();

		double res = 0.0;
		for (final RealType<?> t : input) {
			final double val = t.getRealDouble() - meanVal;
			res += val * val * val * val;
		}
		output.setReal(res / area.getOutput().get());
		return output;
	}

}
