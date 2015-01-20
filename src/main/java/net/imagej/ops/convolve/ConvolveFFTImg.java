package net.imagej.ops.convolve;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.fft.filter.AbstractFFTFilterImg;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;

/**
 * Convolve op for (@link Img) 
 * 
 * @author bnorthan
 *
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Op.class, name = Ops.Convolve.NAME, priority = Priority.VERY_HIGH_PRIORITY)
public class ConvolveFFTImg<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		extends AbstractFFTFilterImg<I, O, K, C> implements Contingent {

	/**
	 * run the filter (ConvolveFFTRAI) on the rais
	 */
	@Override
	public void runFilter(RandomAccessibleInterval<I> raiExtendedInput,
			RandomAccessibleInterval<K> raiExtendedKernel, Img<C> fftImg,
			Img<C> fftKernel, Img<O> output, Interval imgConvolutionInterval) {

		ops.run(ConvolveFFTRAI.class, raiExtendedInput, raiExtendedKernel,
				fftImg, fftKernel, output);

	}
	
	@Override
	public boolean conforms() {
		// TODO: only conforms if the kernel is sufficiently large (else the
		// naive approach should be used) -> what is a good heuristic??
		return Intervals.numElements(kernel) > 9;
	}

}
