package net.imagej.ops.deconvolve;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.fft.filter.AbstractFFTFilterImg;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

/**
 * Richardson Lucy op that operates on (@link Img)
 * 
 * @author bnorthan
 *
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C> 
 */
@Plugin(type = Op.class, name = Ops.Deconvolve.NAME, priority = Priority.HIGH_PRIORITY)
public class RichardsonLucyImg<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		extends AbstractFFTFilterImg<I, O, K, C> {
	
	/**
	 * max number of iterations
	 */
	@Parameter 
	int maxIterations;

	/**
	 * run RichardsonLucyRAI
	 */
	@Override
	public void runFilter(RandomAccessibleInterval<I> raiExtendedInput,
			RandomAccessibleInterval<K> raiExtendedKernel, Img<C> fftImg,
			Img<C> fftKernel, Img<O> output, Interval imgConvolutionInterval) {

		ops.run(RichardsonLucyRAI.class, raiExtendedInput, raiExtendedKernel,
				fftImg, fftKernel, output, true, true, maxIterations,
				imgConvolutionInterval, output.factory());

	}
}
