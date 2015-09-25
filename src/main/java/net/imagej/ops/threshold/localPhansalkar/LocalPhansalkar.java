package net.imagej.ops.threshold.localPhansalkar;

import net.imagej.ops.ComputerOp;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Stats.Mean;
import net.imagej.ops.Ops.Stats.StdDev;
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
 * In this algorithm the threshold is computed as t =
 * mean*(1+p*exp(-q*mean)+k*((stdev/r)-1)) for an image that is normalized to
 * [0, 1].
 * 
 * Phansalkar recommends k = 0.25, r = 0.5, p = 2 and q = 10. In the current
 * implementation, the values of p and q are fixed but can be implemented as
 * additional parameters.
 * 
 * Originally implemented from Phansalkar's paper description by G. Landini
 * (http://fiji.sc/Auto_Local_Threshold#Phansalkar).
 * 
 * Phansalskar N. et al. Adaptive local thresholding for detection of nuclei in
 * diversity stained cytology images. International Conference on Communications
 * and Signal Processing (ICCSP), 2011, 218 - 220.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
@Plugin(type = Ops.Threshold.LocalPhansalkar.class, name = Ops.Threshold.LocalPhansalkar.NAME)
public class LocalPhansalkar<T extends RealType<T>> extends LocalThresholdMethod<T>
	implements Ops.Threshold.LocalPhansalkar
{

	@Parameter(required = false)
	private double k = 0.25;

	@Parameter(required = false)
	private double r = 0.5;

	@Parameter
	private OpService ops;

	// FIXME: Faster calculation of mean and std-dev
	private ComputerOp<Iterable<T>, DoubleType> mean;
	private ComputerOp<Iterable<T>, DoubleType> stdDeviation;

	private double p = 2.0;
	private double q = 10.0;

	@Override
	public void initialize() {
		mean = ops().computer(Mean.class, new DoubleType(), in().getB());
		stdDeviation = ops().computer(StdDev.class, new DoubleType(), in().getB());
	}

	@Override
	public void compute(final Pair<T, Iterable<T>> input, final BitType output) {

		final DoubleType meanValue = new DoubleType();
		mean.compute(input.getB(), meanValue);

		final DoubleType stdDevValue = new DoubleType();
		stdDeviation.compute(input.getB(), stdDevValue);

		double threshold = meanValue.get() * (1.0d + p * Math.exp(-q * meanValue.get()) + k * ((stdDevValue.get()/r) - 1.0));
		
		output.set(input.getA().getRealDouble() >= threshold);
	}

}
