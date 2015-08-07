
package net.imagej.ops.stats;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Stats.MinMax;
import net.imglib2.type.numeric.RealType;

@Plugin(type = StatOp.class, name = MinMax.NAME, label = "Statistics: MinMax")
public class DefaultMinMax<T extends RealType<T>> implements MinMax {

	@Parameter
	private Iterable<T> input;

	@Parameter(type = ItemIO.OUTPUT)
	private T min;

	@Parameter(type = ItemIO.OUTPUT)
	private T max;

	@Override
	public void run() {
		double tmpMin = Double.MAX_VALUE;
		double tmpMax = Double.MIN_VALUE;

		for (final T in : this.input) {
			final double n = in.getRealDouble();

			if (tmpMin > n) {
				tmpMin = n;
			}

			if (tmpMax < n) {
				tmpMax = n;
			}
		}

		this.min = input.iterator().next().createVariable();
		this.min.setReal(tmpMin);
		
		this.max = input.iterator().next().createVariable();
		this.max.setReal(tmpMax);
	}

}
