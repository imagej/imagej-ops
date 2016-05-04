
package net.imagej.ops.filter.fft;

import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.algorithm.fft2.FFTMethods;

/**
 * Utility class that interacts with FFTMethods
 *
 * @author bnorthan
 */
public class FFTMethodsUtility {

	/**
	 * Calculates padding size and complex FFT size for real to complex FFT
	 * 
	 * @param fast if true calculate size for fast FFT
	 * @param inputDimensions original real dimensions
	 * @param paddedDimensions padded real dimensions
	 * @param fftDimensions complex FFT dimensions
	 */
	public static void dimensionsRealToComplex(final boolean fast,
		final Dimensions inputDimensions, final long[] paddedDimensions,
		final long[] fftDimensions)
	{
		if (fast) {
			FFTMethods.dimensionsRealToComplexFast(inputDimensions, paddedDimensions,
				fftDimensions);
		}
		else {
			FFTMethods.dimensionsRealToComplexSmall(inputDimensions, paddedDimensions,
				fftDimensions);
		}
	}

	/**
	 * Calculates padding size size for real to complex FFT
	 * 
	 * @param fast if true calculate size for fast FFT
	 * @param inputDimensions original real dimensions
	 * @return padded real dimensions
	 */
	public static Dimensions getPaddedInputDimensionsRealToComplex(
		final boolean fast, final Dimensions inputDimensions)
	{
		final long[] paddedSize = new long[inputDimensions.numDimensions()];
		final long[] fftSize = new long[inputDimensions.numDimensions()];

		dimensionsRealToComplex(fast, inputDimensions, paddedSize, fftSize);

		return new FinalDimensions(paddedSize);

	}

	/**
	 * Calculates complex FFT size for real to complex FFT
	 * 
	 * @param fast if true calculate size for fast FFT
	 * @param inputDimensions original real dimensions
	 * @return complex FFT dimensions
	 */
	public static Dimensions getFFTDimensionsRealToComplex(final boolean fast,
		final Dimensions inputDimensions)
	{
		final long[] paddedSize = new long[inputDimensions.numDimensions()];
		final long[] fftSize = new long[inputDimensions.numDimensions()];

		dimensionsRealToComplex(fast, inputDimensions, paddedSize, fftSize);

		return new FinalDimensions(fftSize);

	}

}
