
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

	public static void dimensionsRealToComplex(boolean fast,
		Dimensions inputDimensions, long[] paddedDimensions, long[] fftDimensions)
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

	public static Dimensions getPaddedInputDimensionsRealToComplex(boolean fast,
		Dimensions inputDimensions)
	{
		long[] paddedSize = new long[inputDimensions.numDimensions()];
		long[] fftSize = new long[inputDimensions.numDimensions()];

		dimensionsRealToComplex(fast, inputDimensions, paddedSize, fftSize);

		return new FinalDimensions(paddedSize);

	}

	public static Dimensions getFFTDimensionsRealToComplex(boolean fast,
		Dimensions inputDimensions)
	{
		long[] paddedSize = new long[inputDimensions.numDimensions()];
		long[] fftSize = new long[inputDimensions.numDimensions()];

		dimensionsRealToComplex(fast, inputDimensions, paddedSize, fftSize);

		return new FinalDimensions(fftSize);

	}

}
