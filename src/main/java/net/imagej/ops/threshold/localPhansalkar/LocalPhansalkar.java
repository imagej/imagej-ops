package net.imagej.ops.threshold.localPhansalkar;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.stats.mean.MeanOp;
import net.imagej.ops.stats.stdDev.StdDev;
import net.imagej.ops.stats.variance.VarianceOp;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This is a modification of Sauvola's thresholding method to deal with low
 * contrast images.
 * 
 * Phansalskar N. et al. Adaptive local thresholding for detection of nuclei in
 * diversity stained cytology images. International Conference on Communications
 * and Signal Processing (ICCSP), 2011, // 218 - 220.
 * 
 * In this method, the threshold t = mean*(1+p*exp(-q*mean)+k*((stdev/r)-1))
 * 
 * Phansalkar recommends k = 0.25, r = 0.5, p = 2 and q = 10. In the current
 * implementation, the values of p and q are fixed.
 * 
 * Implemented from Phansalkar's paper description by G. Landini.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
@Plugin(type = Ops.Threshold.LocalPhansalkar.class, name = Ops.Threshold.LocalPhansalkar.NAME)
public class LocalPhansalkar<T extends RealType<T>> extends LocalThresholdMethod<T>
	implements Ops.Threshold.LocalPhansalkar
{

	@Parameter
	private double k = 0.25;

	@Parameter
	private double r = 0.5;

	@Parameter
	private OpService ops;

	private MeanOp<Iterable<T>, DoubleType> mean;
	private StdDev<T, DoubleType> stdDeviation;

	private double p = 2.0;
	private double q = 10.0;
	
	@Override
	public void compute(final Pair<T, Iterable<T>> input, final BitType output) {
		if (mean == null) {
			mean = ops.op(MeanOp.class, DoubleType.class, input.getB());
		}
		if (stdDeviation == null) {
			stdDeviation = ops.op(StdDev.class, DoubleType.class, input.getB());
		}

		final DoubleType meanValue = new DoubleType();
		mean.compute(input.getB(), meanValue);

		final DoubleType stdDevValue = new DoubleType();
		stdDeviation.compute(input.getB(), stdDevValue);

		double threshold = meanValue.get() * (1.0d + p * Math.exp(-q * meanValue.get()) + k * ((stdDevValue.get()/r) - 1.0));
		
		output.set(input.getA().getRealDouble() >= threshold);
	}

}
