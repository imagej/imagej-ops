package net.imagej.ops.deconvolve;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.convolve.CorrelateFFTRAI;
import net.imagej.ops.fft.filter.IterativeFFTFilterRAI;

/**
 * Richardson Lucy op that operates on (@link RandomAccessibleInterval)
 * 
 * @author bnorthan
 * 
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Op.class, name = Ops.Deconvolve.NAME, priority = Priority.HIGH_PRIORITY)
public class RichardsonLucyRAI<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
		extends IterativeFFTFilterRAI<I, O, K, C> {

	/**
	 * performs one iteration of the Richardson Lucy Algorithm (Lucy, L. B.
	 * (1974).
	 * "An iterative technique for the rectification of observed distributions".)
	 */
	@Override
	protected void performIteration() {

		// 1. Reblurred will have allready been created in previous iteration

		// 2. divide observed image by reblurred
		inPlaceDivide(raiExtendedReblurred, raiExtendedInput);

		// 3. correlate psf with the output of step 2.
		ops.run(CorrelateFFTRAI.class, raiExtendedReblurred, null, fftInput,
				fftKernel, reblurred, true, false);

		// compute estimate -
		// for standard RL this step will multiply output of correlation step
		// and current estimate
		// (Note: ComputeEstimate can be overridden to achieve regularization)
		ComputeEstimate();

		inPlaceMultiply(output, reblurred);
		
		// TODO
		// normalize for non-circulant deconvolution

	}

	// TODO: replace this function with divide op
	void inPlaceDivide(RandomAccessibleInterval<O> denominatorOutput,
			RandomAccessibleInterval<I> numerator) {

		final Cursor<O> cursorDenominatorOutput = Views.iterable(
				denominatorOutput).cursor();
		final Cursor<I> cursorNumerator = Views.iterable(numerator).cursor();

		while (cursorDenominatorOutput.hasNext()) {
			cursorDenominatorOutput.fwd();
			cursorNumerator.fwd();

			float num = cursorNumerator.get().getRealFloat();
			float div = cursorDenominatorOutput.get().getRealFloat();
			float res = 0;

			if (div > 0) {
				res = num / div;
			} else {
				res = 0;
			}

			cursorDenominatorOutput.get().setReal(res);
		}
	}

	// TODO replace with op
	void inPlaceMultiply(RandomAccessibleInterval<O> inputOutput,
			RandomAccessibleInterval<O> input) {

		final Cursor<O> cursorInputOutput = Views.iterable(inputOutput)
				.cursor();
		final Cursor<O> cursorInput = Views.iterable(input).cursor();

		while (cursorInputOutput.hasNext()) {
			cursorInputOutput.fwd();
			cursorInput.fwd();

			cursorInputOutput.get().mul(cursorInput.get());
		}
	}

	public void ComputeEstimate() {
		inPlaceMultiply(output, reblurred);
	}

}
