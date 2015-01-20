
package net.imagej.ops.fft.size;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.FFTSize;
import net.imglib2.FinalDimensions;

import net.imglib2.algorithm.fft2.FFTMethods;

/**
 * Op to calculate JTransform FFT sizes.
 * 
 * @author bnorthan
 */
@Plugin(type = FFTSize.class, name = FFTSize.NAME)
public class ComputeFFTSize extends AbstractFFTSize {

	public void run() {
		FinalDimensions dim = new FinalDimensions(inputSize);

		if (fast && forward) {

			FFTMethods
				.dimensionsRealToComplexFast(dim, paddedSize, fftSize);

		}
		else if (!fast && forward) {
			FFTMethods.dimensionsRealToComplexSmall(dim, paddedSize,
				fftSize);

		}
		if (fast && !forward) {

			FFTMethods
				.dimensionsComplexToRealFast(dim, paddedSize, fftSize);

		}
		else if (!fast && !forward) {

			FFTMethods.dimensionsComplexToRealSmall(dim, paddedSize,
				fftSize);

		}
	}

}
