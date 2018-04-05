/*-
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

package net.imagej.ops.filter.tubeness;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.gradient.HessianMatrix;
import net.imglib2.algorithm.linalg.eigen.TensorEigenValues;
import net.imglib2.exception.IncompatibleTypeException;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.outofbounds.OutOfBoundsBorderFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.scijava.Cancelable;
import org.scijava.app.StatusService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * The Tubeness filter: enhance filamentous structures of a specified thickness.
 * <p>
 * This filter works on 2D and 3D image exclusively and produces a score for how
 * "tube-like" each point in the image is. This is useful as a preprocessing
 * step for tracing neurons or blood vessels, for example. For 3D image stacks,
 * the filter uses the eigenvalues of the Hessian matrix to calculate this
 * measure of "tubeness", using one of the simpler metrics me mentioned in
 * <u>Sato et al 1997</u>: if the larger two eigenvalues (λ₂ and λ₃) are both
 * negative then value is √(λ₂λ₃), otherwise the value is 0. For 2D images, if
 * the large eigenvalue is negative, we return its absolute value and otherwise
 * return 0.
 * <ul>
 * <li>Source image is filtered first by a gaussian with 𝜎 that sets its scale.
 * <li>The the Hessian matrix is calculated for each pixel.
 * <li>We yield the eigenvalues of the Hessian matrix. The output of the
 * tubeness filter is a combination of these eigenvalues:
 * <ul>
 * <li>in 2D where <code>λ₂</code> is the largest eigenvalue:
 * <code>out = 𝜎 × 𝜎 × |λ₂|</code> if <code>λ₂</code> is negative, 0
 * otherwise.
 * <li>in 3D where <code>λ₂</code> and <code>λ₃</code> are the largest
 * eigenvalues:, <code>out = 𝜎 × 𝜎 × sqrt( λ₂ * λ₃ )</code> if <code>λ₂</code>
 * and <code>λ₃</code> are negative, 0 otherwise.
 * </ul>
 * </ul>
 * This results in enhancing filaments of roughly <code>𝜎 / sqrt(d)</code>
 * thickness.
 * <p>
 * Port of the tubeness filter of the VIB package, with original authors Mark
 * Longair and Stephan Preibisch, to ImageJ-ops.
 *
 * @see <a href=
 *      "https://github.com/fiji/VIB/blob/master/src/main/java/features/Tubeness_.java">Tubeness
 *      VIB plugin code</a>
 * @author Jean-Yves Tinevez
 * @param <T> the type of the source pixels. Must extends {@link RealType}.
 */
@Plugin(type = Ops.Filter.Tubeness.class)
public class DefaultTubeness<T extends RealType<T>> extends
	AbstractUnaryHybridCF<RandomAccessibleInterval<T>, IterableInterval<DoubleType>>
	implements Cancelable, Ops.Filter.Tubeness
{

	@Parameter
	private ThreadService threadService;

	@Parameter
	private StatusService statusService;

	/**
	 * Desired scale in physical units. See {@link #calibration}.
	 */
	@Parameter
	private double sigma;

	/**
	 * Pixel sizes in all dimensions.
	 */
	@Parameter
	private double[] calibration;

	/** Reason for cancelation, or null if not canceled. */
	private String cancelReason;

	@Override
	public Img<DoubleType> createOutput(final RandomAccessibleInterval<T> input) {
		final Img<DoubleType> tubeness = ops().create().img(input,
			new DoubleType());
		return tubeness;
	}

	@Override
	public void compute(final RandomAccessibleInterval<T> input,
		final IterableInterval<DoubleType> tubeness)
	{
		cancelReason = null;

		final int numDimensions = input.numDimensions();
		// Sigmas in pixel units.
		final double[] sigmas = new double[numDimensions];
		for (int d = 0; d < sigmas.length; d++) {
			final double cal = d < calibration.length ? calibration[d] : 1;
			sigmas[d] = sigma / cal;
		}

		/*
		 * Hessian.
		 */

		// Get a suitable image factory.
		final long[] gradientDims = new long[numDimensions + 1];
		final long[] hessianDims = new long[numDimensions + 1];
		for (int d = 0; d < numDimensions; d++) {
			hessianDims[d] = input.dimension(d);
			gradientDims[d] = input.dimension(d);
		}
		hessianDims[numDimensions] = numDimensions * (numDimensions + 1) / 2;
		gradientDims[numDimensions] = numDimensions;
		final Dimensions hessianDimensions = FinalDimensions.wrap(hessianDims);
		final FinalDimensions gradientDimensions = FinalDimensions.wrap(
			gradientDims);
		final ImgFactory<DoubleType> factory = ops().create().imgFactory(
			hessianDimensions);
		final Img<DoubleType> hessian = factory.create(hessianDimensions,
			new DoubleType());
		final Img<DoubleType> gradient = factory.create(gradientDimensions,
			new DoubleType());
		final Img<DoubleType> gaussian = factory.create(input, new DoubleType());

		// Handle multithreading.
		final int nThreads = Runtime.getRuntime().availableProcessors();
		final ExecutorService es = threadService.getExecutorService();

		try {
			// Hessian calculation.
			HessianMatrix.calculateMatrix(Views.extendBorder(input), gaussian,
				gradient, hessian, new OutOfBoundsBorderFactory<>(), nThreads, es,
				sigma);

			statusService.showProgress(1, 3);
			if (isCanceled()) return;

			// Hessian eigenvalues.
			final RandomAccessibleInterval<DoubleType> evs = TensorEigenValues
				.calculateEigenValuesSymmetric(hessian, TensorEigenValues
					.createAppropriateResultImg(hessian, factory, new DoubleType()),
					nThreads, es);

			statusService.showProgress(2, 3);
			if (isCanceled()) return;

			final AbstractUnaryComputerOp<Iterable<DoubleType>, DoubleType> method;
			switch (numDimensions) {
				case 2:
					method = new Tubeness2D(sigma);
					break;
				case 3:
					method = new Tubeness3D(sigma);
					break;
				default:
					System.err.println("Cannot compute tubeness for " + numDimensions +
						"D images.");
					return;
			}
			ops().transform().project(tubeness, evs, method, numDimensions);

			statusService.showProgress(3, 3);

			return;
		}
		catch (final IncompatibleTypeException | InterruptedException
				| ExecutionException e)
		{
			e.printStackTrace();
			return;
		}
	}

	private static final class Tubeness2D extends
		AbstractUnaryComputerOp<Iterable<DoubleType>, DoubleType>
	{

		private final double sigma;

		public Tubeness2D(final double sigma) {
			this.sigma = sigma;
		}

		@Override
		public void compute(final Iterable<DoubleType> input,
			final DoubleType output)
		{
			// Use just the largest one.
			final Iterator<DoubleType> it = input.iterator();
			it.next();
			final double val = it.next().get();
			if (val >= 0.) output.setZero();
			else output.set(sigma * sigma * Math.abs(val));

		}
	}

	private static final class Tubeness3D extends
		AbstractUnaryComputerOp<Iterable<DoubleType>, DoubleType>
	{

		private final double sigma;

		public Tubeness3D(final double sigma) {
			this.sigma = sigma;
		}

		@Override
		public void compute(final Iterable<DoubleType> input,
			final DoubleType output)
		{
			// Use the two largest ones.
			final Iterator<DoubleType> it = input.iterator();
			it.next();
			final double val1 = it.next().get();
			final double val2 = it.next().get();
			if (val1 >= 0. || val2 >= 0.) output.setZero();
			else output.set(sigma * sigma * Math.sqrt(val1 * val2));

		}
	}

	// -- Cancelable methods --

	@Override
	public boolean isCanceled() {
		return cancelReason != null;
	}

	/** Cancels the command execution, with the given reason for doing so. */
	@Override
	public void cancel(final String reason) {
		cancelReason = reason == null ? "" : reason;
	}

	@Override
	public String getCancelReason() {
		return cancelReason;
	}

}
