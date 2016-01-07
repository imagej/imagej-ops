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

import net.imagej.ops.AbstractOp;
import net.imagej.ops.Op;
import net.imagej.ops.math.divide.DivideHandleZero;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

/**
 * Computes Richardson Lucy correction factor for (@link
 * RandomAccessibleInterval) (Lucy, L. B. (1974).
 * "An iterative technique for the rectification of observed distributions".)
 * 
 * @author Brian Northan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
@Plugin(type = Op.class, name = "richardsonlucycorrection",
	priority = Priority.HIGH_PRIORITY)
public class RichardsonLucyCorrection<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractOp
{

	/** buffer to put correction factor in **/
	@Parameter
	RandomAccessibleInterval<O> correction;

	/** oberved image **/
	@Parameter
	RandomAccessibleInterval<O> observed;

	/** reblurred (simulated) image **/
	@Parameter
	RandomAccessibleInterval<O> reblurred;

	/** fft of reblurred (will be computed) **/
	@Parameter
	RandomAccessibleInterval<C> fftBuffer;

	/** fft of kernel (needs to be previously computed) **/
	@Parameter
	RandomAccessibleInterval<C> fftKernel;

	/**
	 * computes the correction factor of the Richardson Lucy Algorithm
	 */
	@Override
	public void run() {

		// divide observed image by reblurred
		ops().run(DivideHandleZero.class, reblurred, observed, reblurred);

		// correlate with psf to compute the correction factor
		ops().filter().correlate(correction, reblurred, reblurred, fftBuffer,
			fftKernel, true, false);

	}

}
