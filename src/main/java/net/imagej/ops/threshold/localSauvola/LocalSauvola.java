package net.imagej.ops.threshold.localSauvola;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.stats.mean.MeanOp;
import net.imagej.ops.stats.variance.VarianceOp;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * This is a modification of Niblack's thresholding method.
 * Sauvola J. and Pietaksinen M. (2000) "Adaptive Document Image Binarization"
 * Pattern Recognition, 33(2): 225-236
 * http://www.ee.oulu.fi/mvg/publications/show_pdf.php?ID=24
 * Ported to ImageJ plugin from E Celebi's fourier_0.8 routines.
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
	private double r = 128.0d;

	@Parameter
	private OpService ops;

	private MeanOp<Iterable<T>, DoubleType> mean;
	private VarianceOp<T, DoubleType> var;

	@Override
	public void compute(final Pair<T, Iterable<T>> input, final BitType output) {
		// Sauvola recommends K_VALUE = 0.5 and R_VALUE = 128.

		if (mean == null) {
			mean = ops.op(MeanOp.class, DoubleType.class, input.getB());
		}
		if (var == null) {
			var = ops.op(VarianceOp.class, DoubleType.class, input.getB());
		}
		
		final DoubleType meanValue = new DoubleType();
		mean.compute(input.getB(), meanValue);
		
		final DoubleType varianceValue = new DoubleType();
		var.compute(input.getB(), varianceValue);
		
		double threshold = meanValue.get() * (1.0d + k * ((Math.sqrt(varianceValue.get())/r) - 1.0));
		
		output.set(input.getA().getRealDouble() >= threshold);
	}

}
