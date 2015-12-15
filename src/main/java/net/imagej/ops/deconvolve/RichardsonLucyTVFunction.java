/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
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

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.filter.AbstractFFTFilter;
import net.imagej.ops.special.AbstractUnaryComputerOp;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

/**
 * Richardson Lucy with total variation op that operates on (@link Img)
 * Richardson-Lucy algorithm with total variation regularization for 3D confocal
 * microscope deconvolution Microsc Res Rech 2006 Apr; 69(4)- 260-6
 * 
 * @author bnorthan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Ops.Deconvolve.RichardsonLucyTV.class,
	priority = Priority.HIGH_PRIORITY)
public class RichardsonLucyTVFunction<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractFFTFilter<I, O, K, C> implements
	Ops.Deconvolve.RichardsonLucyTV
{

	@Parameter
	OpService ops;

	/**
	 * max number of iterations
	 */
	@Parameter
	int maxIterations;

	/**
	 * the regularization factor determines smoothness of solution
	 */
	@Parameter
	float regularizationFactor = 0.01f;

	/**
	 * indicates whether to use non-circulant edge handling
	 */
	@Parameter(required = false)
	private boolean nonCirculant = false;

	/**
	 * indicates whether to use acceleration
	 */
	@Parameter(required = false)
	private boolean accelerate = false;

	/**
	 * run RichardsonLucyTVRAI
	 */
	@Override
	public void runFilter(RandomAccessibleInterval<I> raiExtendedInput,
		RandomAccessibleInterval<K> raiExtendedKernel,
		RandomAccessibleInterval<C> fftImg, RandomAccessibleInterval<C> fftKernel,
		RandomAccessibleInterval<O> output, Interval imgConvolutionInterval)
	{

		// create Richardson Lucy TV update op, this will override the base RL
		// Update.
		@SuppressWarnings("unchecked")
		AbstractUnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> computeEstimateOp =
			ops.op(RichardsonLucyTVUpdateRAI.class, output, output,
				regularizationFactor);

		if (nonCirculant == false) {
			ops().run(RichardsonLucyCirculantRAI.class, output, raiExtendedInput,
				raiExtendedKernel, fftImg, fftKernel, true, true, maxIterations,
				imgConvolutionInterval, getOutFactory(), computeEstimateOp, accelerate);
		}
		else {

			ops().run(RichardsonLucyNonCirculantRAI.class, output, raiExtendedInput,
				raiExtendedKernel, fftImg, fftKernel, true, true, maxIterations,
				imgConvolutionInterval, getOutFactory(), computeEstimateOp, accelerate,
				in(), getKernel());
		}
	}
}
