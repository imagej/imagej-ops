/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.ops.deconvolve;

import net.imagej.ops.Ops;
import net.imagej.ops.filter.IterativeFFTFilterRAI;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Richardson Lucy op that operates on (@link RandomAccessibleInterval) (Lucy,
 * L. B. (1974).
 * "An iterative technique for the rectification of observed distributions".)
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Ops.Deconvolve.RichardsonLucy.class,
	priority = Priority.HIGH_PRIORITY)
public class RichardsonLucyRAI<I extends RealType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends IterativeFFTFilterRAI<I, O, K, C> implements Ops.Deconvolve.RichardsonLucy
{

	@Override
	protected void initialize() {
		super.initialize();

	}

	/**
	 * performs one iteration of the Richardson Lucy Algorithm
	 */
	@Override
	protected void performIteration() {

		// 1. Create Reblurred (this step will have already been done from the
		// previous iteration in order to calculate error stats)

		// 2. divide observed image by reblurred
		inPlaceDivide(getRAIExtendedReblurred(), getRAIExtendedInput());

		// 3. correlate psf with the output of step 2.
		ops().filter().correlate(getRAIExtendedReblurred(), null, getFFTInput(),
			getFFTKernel(), getRAIExtendedReblurred(), true, false);

		// compute estimate -
		// for standard RL this step will multiply output of correlation step
		// and current estimate
		// (Note: ComputeEstimate can be overridden to achieve regularization)
		ComputeEstimate();

		// normalize for non-circulant deconvolution
		if (getNonCirculant()) {
			inPlaceDivide2(getNormalization(), getRAIExtendedEstimate());
		}

	}

	public void ComputeEstimate() {
		inPlaceMultiply(getRAIExtendedEstimate(), getRAIExtendedReblurred());
	}

	// TODO: replace this function with divide op
	protected void inPlaceDivide(RandomAccessibleInterval<O> denominatorOutput,
		RandomAccessibleInterval<I> numerator)
	{

		final Cursor<O> cursorDenominatorOutput =
			Views.iterable(denominatorOutput).cursor();
		final Cursor<I> cursorNumerator = Views.iterable(numerator).cursor();

		while (cursorDenominatorOutput.hasNext()) {
			cursorDenominatorOutput.fwd();
			cursorNumerator.fwd();

			float num = cursorNumerator.get().getRealFloat();
			float div = cursorDenominatorOutput.get().getRealFloat();
			float res = 0;

			if (div > 0) {
				res = num / div;
			}
			else {
				res = 0;
			}

			cursorDenominatorOutput.get().setReal(res);
		}
	}

	// TODO: replace this function with divide op
	protected void inPlaceDivide2(RandomAccessibleInterval<O> denominator,
		RandomAccessibleInterval<O> numeratorOutput)
	{

		final Cursor<O> cursorDenominator = Views.iterable(denominator).cursor();
		final Cursor<O> cursorNumeratorOutput =
			Views.iterable(numeratorOutput).cursor();

		while (cursorDenominator.hasNext()) {
			cursorDenominator.fwd();
			cursorNumeratorOutput.fwd();

			float num = cursorNumeratorOutput.get().getRealFloat();
			float div = cursorDenominator.get().getRealFloat();
			float res = 0;

			if (div > 0) {
				res = num / div;
			}
			else {
				res = 0;
			}

			cursorNumeratorOutput.get().setReal(res);
		}
	}

	// TODO replace with op
	protected void inPlaceMultiply(RandomAccessibleInterval<O> inputOutput,
		RandomAccessibleInterval<O> input)
	{

		final Cursor<O> cursorInputOutput = Views.iterable(inputOutput).cursor();
		final Cursor<O> cursorInput = Views.iterable(input).cursor();

		while (cursorInputOutput.hasNext()) {
			cursorInputOutput.fwd();
			cursorInput.fwd();

			cursorInputOutput.get().mul(cursorInput.get());
		}
	}
}
