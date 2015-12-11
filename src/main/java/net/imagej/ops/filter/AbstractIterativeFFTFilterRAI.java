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

package net.imagej.ops.filter;

import org.scijava.app.StatusService;
import org.scijava.plugin.Parameter;

import net.imagej.ops.deconvolve.accelerate.Accelerator;
import net.imagej.ops.deconvolve.accelerate.VectorAccelerator;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

/**
 * Abstract class for iterative FFT filters that perform on RAI. Boundary
 * conditions are handled by the scheme described at:
 * http://bigwww.epfl.ch/deconvolution/challeng public Interval
 * getImgConvolutionInterval() { return imgConvolutionInterval; } public void
 * setImgConvolutionInterval(Interval imgConvolutionInterval) {
 * this.imgConvolutionInterval = imgConvolutionInterval; }
 * e2013/index.html?p=doc_math_rl)
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class AbstractIterativeFFTFilterRAI<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractFFTFilterRAI<I, O, K, C>
{

	@Parameter(required = false)
	private StatusService status;

	/**
	 * Max number of iterations to perform
	 */
	@Parameter
	private int maxIterations;

	/**
	 * The interval to process TODO: this is probably redundant - remove
	 */
	@Parameter
	private Interval imgConvolutionInterval;

	/**
	 * The ImgFactory used to create output and intermediate images
	 */
	@Parameter
	private ImgFactory<O> imgFactory;

	/**
	 * TODO: make this an op A boolean which indicates whether to perform
	 * acceleration
	 */
	@Parameter(required = false)
	private boolean accelerate = false;

	/**
	 * An OutOfBoundsFactory which defines the extension strategy
	 */
	@Parameter(required = false)
	private OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput;

	private RandomAccessibleInterval<O> raiExtendedReblurred;

	private RandomAccessibleInterval<O> raiExtendedEstimate;

	private Accelerator<O> accelerator = null;

	@Override
	public void compute(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<O> out)
	{

		performIterations();

	}

	@Override
	public void initialize() {

		initializeImages();

		if (getAccelerate()) {
			accelerator = new VectorAccelerator<O>(this.getImgFactory());
		}

	}

	abstract protected void initializeImages();

	protected void performIterations() {

		createReblurred();

		for (int i = 0; i < maxIterations; i++) {

			if (status != null) {
				status.showProgress(i, maxIterations);
			}
			performIteration();

			// accelerate
			if (getAccelerate()) {
				getAccelerator().Accelerate(getRAIExtendedEstimate());
			}

			createReblurred();

		}
	}

	/**
	 * convolve estimate with kernel to create reblurred
	 */
	protected void createReblurred() {

		ops().filter().convolve(raiExtendedReblurred, raiExtendedEstimate,
			getRAIExtendedKernel(), getFFTInput(), getFFTKernel(), true, false);

	}

	/**
	 * perform one iteration of the algorithm. Sub-classes need to implement this
	 * function.
	 */
	abstract protected void performIteration();

	protected RandomAccessibleInterval<O> getRAIExtendedReblurred() {
		return raiExtendedReblurred;
	}

	protected void setRAIExtendedReblurred(
		RandomAccessibleInterval<O> raiExtendedReblurred)
	{
		this.raiExtendedReblurred = raiExtendedReblurred;
	}

	protected RandomAccessibleInterval<O> getRAIExtendedEstimate() {
		return raiExtendedEstimate;
	}

	protected void setRAIExtendedEstimate(
		RandomAccessibleInterval<O> raiExtendedEstimate)
	{
		this.raiExtendedEstimate = raiExtendedEstimate;
	}

	protected ImgFactory<O> getImgFactory() {
		return imgFactory;
	}

	public OutOfBoundsFactory<O, RandomAccessibleInterval<O>> getObfOutput() {
		return obfOutput;
	}

	public void setObfOutput(
		OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput)
	{
		this.obfOutput = obfOutput;
	}

	public Interval getImgConvolutionInterval() {
		return imgConvolutionInterval;
	}

	public void setImgConvolutionInterval(Interval imgConvolutionInterval) {
		this.imgConvolutionInterval = imgConvolutionInterval;
	}

	public boolean getAccelerate() {
		return accelerate;
	}

	public Accelerator<O> getAccelerator() {
		return accelerator;
	}

}
