package net.imagej.ops.fft.filter;

import org.scijava.plugin.Parameter;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

/**
 * Abstract class for FFT based filters that operate on RAI
 * 
 * @author bnorthan
 * 
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class AbstractFFTFilterRAI<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		implements Op {

	@Parameter
	protected OpService ops;

	/**
	 * input rai. If extension is desired it needs to be done before passing the
	 * rai to the op
	 */
	@Parameter
	protected RandomAccessibleInterval<I> raiExtendedInput;

	/**
	 * kernel rai. Needs to be the same size as the input rai
	 */
	@Parameter(required = false)
	protected RandomAccessibleInterval<K> raiExtendedKernel;

	/**
	 * Img to be used to store FFTs for input. Size of fftInput must correspond
	 * to the fft size of raiExtendedInput
	 */
	@Parameter(required = false)
	protected Img<C> fftInput;

	/**
	 * Img to be used to store FFTs for kernel. Size of fftKernel must
	 * correspond to the fft size of raiExtendedKernel
	 */
	@Parameter(required = false)
	protected Img<C> fftKernel;

	/**
	 * RAI to store output
	 */
	@Parameter(required = false)
	protected RandomAccessibleInterval<O> output;

	/**
	 * Boolean indicating that the input FFT has allready been calculated (use
	 * when re-using an input with the same kernel size)
	 */
	@Parameter(required = false)
	protected boolean performInputFFT = true;

	/**
	 * Boolean indicating that the kernel FFT has allready been calculated (use
	 * when re-using an input with the same kernel size)
	 */
	@Parameter(required = false)
	protected boolean performKernelFFT = true;

}
