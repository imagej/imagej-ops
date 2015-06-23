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

import net.imagej.ops.OpService;
import net.imagej.ops.filter.convolve.ConvolveFFTRAI;
import net.imagej.ops.filter.correlate.CorrelateFFTRAI;
import net.imglib2.Cursor;
import net.imglib2.Interval;
import net.imglib2.Point;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.Views;

import org.scijava.Context;
import org.scijava.plugin.Parameter;

/**
 * Abstract class for iterative FFT filters that perform on RAI
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class IterativeFFTFilterRAI<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractFFTFilterRAI<I, O, K, C>
{

	@Parameter
	private OpService ops;

	@Parameter
	Context ctx;

	/**
	 * Max number of iterations to perform
	 */
	@Parameter
	private int maxIterations;

	/**
	 * The interval to process
	 */
	@Parameter
	private Interval imgConvolutionInterval;

	/**
	 * The ImgFactory used to create images
	 */
	@Parameter
	private ImgFactory<O> imgFactory;

	/**
	 * TODO: review! - k is the size of the measurement window. That is the size
	 * of the acquired image before extension k is required to calculate the
	 * non-circulant normalization factor
	 */
	@Parameter(required = false)
	private long[] k;

	/**
	 * TODO: review! - l is the size of the psf. l is required to calculate the
	 * non-circulant normalization factor
	 */
	@Parameter(required = false)
	private long[] l;

	/**
	 * TODO: review boolean which indicates whether to perform non-circulant
	 * deconvolution
	 */
	@Parameter(required = false)
	private boolean nonCirculant = false;

	/**
	 * TODO: review boolean which indicates whether to perform acceleration
	 */
	@Parameter(required = false)
	private boolean accelerate = false;

	/**
	 * TODO: review An OutOfBoundsFactory which defines the extension strategy
	 */
	@Parameter(required = false)
	private OutOfBoundsFactory<O, RandomAccessibleInterval<O>> obfOutput;

	private RandomAccessibleInterval<O> raiExtendedReblurred;

	private RandomAccessibleInterval<O> raiExtendedEstimate;

	private Img<O> reblurred;

	// Normalization factor for edge handling (see
	// http://bigwww.epfl.ch/deconvolution/challenge2013/index.html?p=doc_math_rl)
	private Img<O> normalization = null;

	@Override
	public void run() {

		initialize();

		performIterations(maxIterations);

	}

	/**
	 * initialize TODO: review this function
	 */
	protected void initialize() {

		// if no output out of bounds factory exists create the obf for output
		if (getObfOutput() == null) {
			setObfOutput(new OutOfBoundsConstantValueFactory<O, RandomAccessibleInterval<O>>(
				Util.getTypeFromInterval(getOutput()).createVariable()));
		}

		Type<O> outType = Util.getTypeFromInterval(getOutput());

		// create image for the reblurred
		reblurred = imgFactory.create(getOutput(), outType.createVariable());

		// TODO: review this step
		// extend the output and use it as a buffer to store the estimate
		raiExtendedEstimate =
			Views.interval(Views.extend(getOutput(), getObfOutput()),
				getImgConvolutionInterval());

		// assemble the extended view of the reblurred
		raiExtendedReblurred =
			Views.interval(Views.extend(reblurred, getObfOutput()),
				getImgConvolutionInterval());

		// perform fft of input
		ops.filter().fft(getFFTInput(), getRAIExtendedInput());

		// perform fft of psf
		ops.filter().fft(getFFTKernel(), getRAIExtendedKernel());

		// if non-circulant decon mode create image for normalization
		if (nonCirculant) {
			normalization =
				getImgFactory().create(raiExtendedEstimate, outType.createVariable());

			this.CreateNormalizationImageSemiNonCirculant();
		}

		// set first guess of estimate
		// TODO: implement logic for various first guesses.
		// for now just set to original image
		Cursor<O> c = Views.iterable(raiExtendedEstimate).cursor();
		Cursor<I> cIn = Views.iterable(getRAIExtendedInput()).cursor();

		// ops
		// .run("correlate", normalization, normalization, getRaiExtendedKernel(),
		// new int[]{0,0,0});
		while (c.hasNext()) {
			c.fwd();
			cIn.fwd();
			c.get().setReal(cIn.get().getRealFloat());
		}

		// TODO: see if we still need to unmask values outside fft space
		/*		// fft space can be slightly larger then the object space so so use a mask
			// to get
			// rid of any values outside the object space.
			// StaticFunctions.InPlaceMultiply(normalization, mask);
			*/

		//
		createReblurred();

		// TODO: code for edge handling scheme
	}

	void performIterations(int maxIterations) {
		for (int i = 0; i < maxIterations; i++) {
			performIteration();
			createReblurred();
		}
	}

	protected void createReblurred() {
		// perform convolution -- kernel FFT should allready exist
		ops.filter().convolve(raiExtendedEstimate, null, getFFTInput(),
			getFFTKernel(), raiExtendedReblurred, true, false);
	}

	/**
	 * create the normalization image needed for semi noncirculant model see
	 * http://bigwww.epfl.ch/deconvolution/challenge2013/index.html?p=doc_math_rl
	 */
	protected void CreateNormalizationImageSemiNonCirculant() {

		final RandomAccessibleInterval<O> raiExtendedReblurred =
			getRAIExtendedReblurred();

		// k is the window size (valid image region)
		int length = k.length;

		long[] n = new long[length];

		// n is the valid image size plus the extended region
		// also referred to as object space size
		for (int d = 0; d < length; d++) {
			n[d] = raiExtendedReblurred.dimension(d);
		}

		// create the normalization image
		final O type = Util.getTypeFromInterval(raiExtendedReblurred);
		normalization = getImgFactory().create(raiExtendedReblurred, type);

		// size of the measurement window
		Point size = new Point(3);
		size.setPosition(k[0], 0);
		size.setPosition(k[1], 1);
		size.setPosition(k[2], 2);

		// starting point of the measurement window when it is centered in fft space
		Point start = new Point(3);
		start.setPosition((n[0] - k[0]) / 2, 0);
		start.setPosition((n[1] - k[1]) / 2, 1);
		start.setPosition((n[2] - k[2]) / 2, 2);

		// draw a cube the size of the measurement space
		drawCube(normalization, start, size, 1.0);

		// 3. correlate psf with the output of step 2.
		ops.run(CorrelateFFTRAI.class, normalization, null, getFFTInput(),
			getFFTKernel(), normalization, true, false);

		try {
			// saver.saveImg("/home/bnorthan/Brian2014/Projects/deconware2/cube_2.tif",
			// normalization);

		}
		catch (Exception ex) {

		}

	}

	/**
	 * perform one iteration of the algorithm. Sub-classes need to implement this
	 * function.
	 */
	abstract protected void performIteration();

	/**
	 * TODO: Make this function an op or separate into utility
	 * 
	 * @param randomAccessible
	 * @param position
	 * @param intensity
	 */
	public static <T extends RealType<T>> void drawPoint(
		final RandomAccessibleInterval<T> randomAccessible, final Point position,
		final double intensity)
	{
		RandomAccess<T> randomAccess = randomAccessible.randomAccess();

		randomAccess.setPosition(position);

		randomAccess.get().setReal(intensity);
	}

	/**
	 * TODO: Make this function an op or separate into utility
	 * 
	 * @param randomAccessible
	 * @param start
	 * @param size
	 * @param intensity
	 */
	public static <T extends RealType<T>> void drawCube(
		final RandomAccessibleInterval<T> randomAccessible, final Point start,
		final Point size, final double intensity)
	{
		// assume 2-D or 3-D space for now
		if (start.numDimensions() == 2) {
			Point position = new Point(2);

			int yStart = start.getIntPosition(1);
			int xStart = start.getIntPosition(0);

			for (int y = 0; y < size.getIntPosition(1); y++) {
				position.setPosition(y + yStart, 1);
				for (int x = 0; x < size.getIntPosition(0); x++) {
					position.setPosition(x + xStart, 0);
					drawPoint(randomAccessible, position, intensity);
				}
			}
		}
		if (start.numDimensions() == 3) {
			Point position = new Point(3);

			int zStart = start.getIntPosition(2);
			int yStart = start.getIntPosition(1);
			int xStart = start.getIntPosition(0);

			for (int z = 0; z < size.getIntPosition(2); z++) {
				position.setPosition(z + zStart, 2);
				for (int y = 0; y < size.getIntPosition(1); y++) {
					position.setPosition(y + yStart, 1);
					for (int x = 0; x < size.getIntPosition(0); x++) {
						position.setPosition(x + xStart, 0);
						drawPoint(randomAccessible, position, intensity);
					}
				}
			}
		}

	}

	protected RandomAccessibleInterval<O> getRAIExtendedReblurred() {
		return raiExtendedReblurred;
	}

	protected RandomAccessibleInterval<O> getRAIExtendedEstimate() {
		return raiExtendedEstimate;
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

	public long[] getK() {
		return k;
	}

	public long[] getL() {
		return l;
	}

	public boolean getNonCirculant() {
		return nonCirculant;
	}

	public Img<O> getNormalization() {
		return normalization;
	}

	public boolean getAccelerate() {
		return accelerate;
	}

}
