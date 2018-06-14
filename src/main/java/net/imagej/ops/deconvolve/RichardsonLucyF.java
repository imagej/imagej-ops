/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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

import java.util.ArrayList;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.deconvolve.accelerate.VectorAccelerator;
import net.imagej.ops.filter.AbstractFFTFilterF;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.inplace.Inplaces;
import net.imagej.ops.special.inplace.UnaryInplaceOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory.Boundary;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Richardson Lucy function op that operates on (@link RandomAccessibleInterval)
 * (Lucy, L. B. (1974). "An iterative technique for the rectification of
 * observed distributions".)
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Ops.Deconvolve.RichardsonLucy.class, priority = Priority.HIGH)
public class RichardsonLucyF<I extends RealType<I> & NativeType<I>, O extends RealType<O> & NativeType<O>, K extends RealType<K> & NativeType<K>, C extends ComplexType<C> & NativeType<C>>
	extends AbstractFFTFilterF<I, O, K, C> implements
	Ops.Deconvolve.RichardsonLucy
{

	@Parameter
	private OpService ops;

	/**
	 * max number of iterations
	 */
	@Parameter
	private int maxIterations;

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

	private UnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> computeEstimateOp;

	private UnaryInplaceOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> normalizer;

	@Override
	public void initialize() {

		// the out of bounds factory will be different depending on wether we are
		// using circulant or non-circulant
		if (this.getOBFInput() == null) {

			if (!nonCirculant) {
				setOBFInput(new OutOfBoundsMirrorFactory<>(Boundary.SINGLE));
			}
			else if (nonCirculant) {
				setOBFInput(new OutOfBoundsConstantValueFactory<>(Util
					.getTypeFromInterval(in()).createVariable()));
			}
		}

		computeEstimateOp = getComputeEstimateOp();

		super.initialize();

	}

	/**
	 * create a richardson lucy filter
	 */
	@Override
	@SuppressWarnings("unchecked")
	public
		BinaryComputerOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<K>, RandomAccessibleInterval<O>>
		createFilterComputer(RandomAccessibleInterval<I> raiExtendedInput,
			RandomAccessibleInterval<K> raiExtendedKernel,
			RandomAccessibleInterval<C> fftImg, RandomAccessibleInterval<C> fftKernel,
			RandomAccessibleInterval<O> output)
	{
		UnaryInplaceOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>> accelerator =
			null;

		if (accelerate == true) {
			accelerator =
				(UnaryInplaceOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>>) Inplaces
					.unary(ops(), VectorAccelerator.class, output);
		}

		// if non-circulant mode, set up the richardson-lucy computer in
		// non-circulant mode and return it
		if (nonCirculant) {
			normalizer =
				(UnaryInplaceOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>>) Inplaces
					.unary(ops(), NonCirculantNormalizationFactor.class, output, in(),
						in2(), fftImg, fftKernel);

			ArrayList<UnaryInplaceOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>>> list =
				new ArrayList<>();

			list.add(normalizer);

			// set up the noncirculant first guess op (a flat sheet with total sum
			// normalized by image area)
			UnaryFunctionOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>> fg =
				(UnaryFunctionOp) Functions.unary(ops(), NonCirculantFirstGuess.class,
					RandomAccessibleInterval.class, RandomAccessibleInterval.class, Util
						.getTypeFromInterval(output), in());

			return Computers.binary(ops(), RichardsonLucyC.class, output,
				raiExtendedInput, raiExtendedKernel, fftImg, fftKernel, true, true,
				maxIterations, accelerator, computeEstimateOp, fg.calculate(
					raiExtendedInput), list);
		}

		// return a richardson lucy computer
		return Computers.binary(ops(), RichardsonLucyC.class, output,
			raiExtendedInput, raiExtendedKernel, fftImg, fftKernel, true, true,
			maxIterations, accelerator, computeEstimateOp);
	}

	/**
	 * set up and return the compute estimate op. This function can be over-ridden
	 * to implement different types of richardson lucy (like total variation
	 * richardson lucy)
	 * 
	 * @return compute estimate op
	 */
	protected
		UnaryComputerOp<RandomAccessibleInterval<O>, RandomAccessibleInterval<O>>
		getComputeEstimateOp()
	{
		return (UnaryComputerOp) Computers.unary(ops(), RichardsonLucyUpdate.class,
			RandomAccessibleInterval.class, RandomAccessibleInterval.class);
	}

}
