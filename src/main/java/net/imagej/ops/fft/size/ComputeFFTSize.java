
package net.imagej.ops.fft.size;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.FftSize;
import net.imglib2.FinalDimensions;

import net.imglib2.algorithm.fft2.FFTMethods;

/**
 * Op to calculate JTransform fft sizes
 * 
 * @author bnorthan
 */
@Plugin(type = FftSize.class, name = FftSize.NAME)
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
