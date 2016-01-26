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

import net.imagej.ops.special.inplace.UnaryInplaceOp;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgFactory;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.app.StatusService;
import org.scijava.plugin.Parameter;

/**
 * Abstract class for iterative FFT filters that perform on RAI. Boundary
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class AbstractIterativeFFTFilterC<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends
	AbstractFFTFilterC<RandomAccessibleInterval<I>, RandomAccessibleInterval<O>, RandomAccessibleInterval<K>, RandomAccessibleInterval<C>>
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
	 * TODO: make this an op?? A boolean which indicates whether to perform
	 * acceleration
	 */
	@Parameter(required = false)
	private UnaryInplaceOp<RandomAccessibleInterval<O>> accelerator;

	private RandomAccessibleInterval<O> raiExtendedReblurred;

	private RandomAccessibleInterval<O> raiExtendedEstimate;

	@Override
	public void compute2(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<K> kernel, RandomAccessibleInterval<O> out)
	{

		preProcess(in, kernel, out);

		performIterations(in, kernel, out);

		postProcess(in, kernel, out);

	}

	@Override
	public void initialize() {
		super.initialize();
	}

	abstract protected void preProcess(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<K> kernel, RandomAccessibleInterval<O> out);

	abstract protected void performIterations(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<K> kernel, RandomAccessibleInterval<O> out);

	protected void postProcess(RandomAccessibleInterval<I> in,
		RandomAccessibleInterval<K> kernel, RandomAccessibleInterval<O> out)
	{}

	/**
	 * convolve estimate with kernel to create reblurred
	 */
	protected void createReblurred() {

		ops().filter().convolve(raiExtendedReblurred, raiExtendedEstimate, in2(),
			getFFTInput(), getFFTKernel(), true, false);

	}

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

	public Interval getImgConvolutionInterval() {
		return imgConvolutionInterval;
	}

	public void setImgConvolutionInterval(Interval imgConvolutionInterval) {
		this.imgConvolutionInterval = imgConvolutionInterval;
	}

	public UnaryInplaceOp<RandomAccessibleInterval<O>> getAccelerator() {
		return accelerator;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

}
