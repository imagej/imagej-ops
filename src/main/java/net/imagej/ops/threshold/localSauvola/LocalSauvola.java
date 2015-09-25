package net.imagej.ops.threshold.localSauvola;

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
 * This is a modification of Niblack's thresholding method. In contrast to the
 * recommendation on parameters in the publication, this implementation operates
 * on normalized images (to the [0, 1] range). Hence, the r parameter defaults
 * to half the possible standard deviation in a normalized image, namely 0.5
 * 
 * Sauvola J. and Pietaksinen M. (2000) "Adaptive Document Image Binarization"
 * Pattern Recognition, 33(2): 225-236
 * 
 * http://www.ee.oulu.fi/mvg/publications/show_pdf.php?ID=24
 * 
 * Original ImageJ1 implementation by Gabriel Landini.
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
@Plugin(type = Ops.Threshold.LocalSauvola.class, name = Ops.Threshold.LocalSauvola.NAME)
public class LocalSauvola<T extends RealType<T>> extends LocalThresholdMethod<T>
	implements Ops.Threshold.LocalSauvola
{

	@Parameter
	private double k = 0.5d;

	@Parameter
	private double r = 0.5d;

	@Parameter
	private OpService ops;

	private ComputerOp<Iterable<T>, DoubleType> mean;
	private ComputerOp<Iterable<T>, DoubleType> stdDeviation;

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

		double threshold = meanValue.get() * (1.0d + k * ((Math.sqrt(stdDevValue.get())/r) - 1.0));

		output.set(input.getA().getRealDouble() >= threshold);
	}

}
