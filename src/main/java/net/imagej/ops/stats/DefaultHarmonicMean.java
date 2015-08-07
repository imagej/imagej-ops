
package net.imagej.ops.stats;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.HarmonicMean;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = HarmonicMean.NAME,
	label = "Statistics: Harmonic Mean")
public class DefaultHarmonicMean<T extends RealType<T>, O extends RealType<O>> extends
	AbstractStatOp<Iterable<T>, O>implements HarmonicMean
{

	@Override
	public void compute(final Iterable<T> input, final O output) {
		final double area = this.ops.stats().size(input).getRealDouble();
		final double sumOfInverses = this.ops.stats().size(input).getRealDouble();

		if (sumOfInverses != 0) {
			output.setReal(area / sumOfInverses);
		}
		else {
			output.setReal(0);
		}
	}
}
