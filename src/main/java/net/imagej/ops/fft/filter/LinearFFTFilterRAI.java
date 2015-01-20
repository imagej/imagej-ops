
package net.imagej.ops.fft.filter;

import net.imglib2.img.Img;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

/**
 * Abstract class for linear filters that operate on RAIs
 * 
 * @author bnorthan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class LinearFFTFilterRAI<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractFFTFilterRAI<I, O, K, C>
{

	@Override
	public void run() {

		// perform input FFT if needed
		if (performInputFFT) {
			ops.run("fft", fftInput, raiExtendedInput);
		}

		// perform kernel FFT if needed
		if (performKernelFFT) {
			ops.run("fft", fftKernel, raiExtendedKernel);
		}

		// perform the operation in frequency domain (ie multiplication for
		// convolution, complex conjugate multiplication for correlation,
		// etc.) Wiener Filter,
		frequencyOperation(fftInput, fftKernel);

		// inverse fft
		ops.run("ifft", output, fftInput);
	}

	// abstract function that implements an operation in frequency domain (ie
	// multiplication for convolution,
	// complex conjugate multiplication for correlation, Wiener Filter, etc.)
	protected abstract void frequencyOperation(final Img<C> a, final Img<C> b);
}
