package net.imagej.ops.threshold.sauvola;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.stats.mean.MeanOp;
import net.imagej.ops.stats.variance.VarianceOp;
import net.imagej.ops.threshold.LocalThresholdMethod;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.RankFilters;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
@Plugin(type = Ops.Threshold.Sauvola.class, name = Ops.Threshold.Sauvola.NAME)
public class Sauvola<T extends RealType<T>> extends LocalThresholdMethod<T>
	implements Ops.Threshold.Sauvola
{

	@Parameter
	private double k = 0.5d;
	
	@Parameter
	private double r = 128.0d;

	@Parameter
	boolean doIwhite = true;
	
	@Parameter
	private OpService ops;

	private MeanOp<Iterable<T>, DoubleType> mean;
	private VarianceOp<T, DoubleType> var;

	@Override
	public void compute(final Pair<T> input, final BitType output) {
		// Sauvola recommends K_VALUE = 0.5 and R_VALUE = 128.
		// This is a modification of Niblack's thresholding method.
		// Sauvola J. and Pietaksinen M. (2000) "Adaptive Document Image Binarization"
		// Pattern Recognition, 33(2): 225-236
		// http://www.ee.oulu.fi/mvg/publications/show_pdf.php?ID=24
		// Ported to ImageJ plugin from E Celebi's fourier_0.8 routines
		// This version uses a circular local window, instead of a rectagular one		
		if (mean == null) {
			mean = ops.op(MeanOp.class, DoubleType.class, input.neighborhood);
		}
		if (var == null) {
			var = ops.op(VarianceOp.class, DoubleType.class, input.neighborhood);
		}
		
		final DoubleType meanValue = new DoubleType();
		mean.compute(input.neighborhood, meanValue);
		
		final DoubleType varianceValue = new DoubleType();
		var.compute(input.neighborhood, varianceValue);
		
		double threshold = meanValue.get() * (1.0d + k * ((Math.sqrt(varianceValue.get())/r) - 1.0));
		
		if (doIwhite){
			output.set(input.pixel.getRealDouble() > threshold);
		}
		else {
			output.set(input.pixel.getRealDouble() < threshold);
		}
	}

}
