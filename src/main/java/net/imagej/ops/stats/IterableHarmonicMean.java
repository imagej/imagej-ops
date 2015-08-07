
package net.imagej.ops.stats;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.HarmonicMean;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = HarmonicMean.NAME,
	label = "Statistics: Harmonic Mean", priority = Priority.FIRST_PRIORITY)
public class IterableHarmonicMean<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements HarmonicMean
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		double size = 0;
		double sumOfInverses = 0;
		for (final T in : input) {
			size++;
			sumOfInverses += (1.0d / in.getRealDouble());
		}

		if (sumOfInverses != 0) {
			output.setReal(size / sumOfInverses);
		}
		else {
			output.setReal(0);
		}
	}
}
