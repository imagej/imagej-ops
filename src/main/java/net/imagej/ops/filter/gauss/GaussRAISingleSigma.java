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

package net.imagej.ops.filter.gauss;

import java.util.Arrays;

import net.imagej.ops.Ops;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Gaussian filter which can be called with single sigma, i.e. the sigma is the
 * same in each dimension.
 * 
 * @author Christian Dietz, University of Konstanz
 * @param <T> type of input
 * @param <V> type of output
 */
@SuppressWarnings({ "unchecked" })
@Plugin(type = Ops.Filter.Gauss.class)
public class GaussRAISingleSigma<T extends RealType<T>, V extends RealType<V>>
	extends
	AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<V>>
	implements Ops.Filter.Gauss
{

	@Parameter
	private double sigma;

	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBounds;

	@Override
	public void compute1(final RandomAccessibleInterval<T> input,
		final RandomAccessibleInterval<V> output)
	{
		final double[] sigmas = new double[input.numDimensions()];
		Arrays.fill(sigmas, sigma);

		ops().filter().gauss(output, input, sigmas, outOfBounds);
	}

	@Override
	public RandomAccessibleInterval<V> createOutput(
		final RandomAccessibleInterval<T> input)
	{
		return (RandomAccessibleInterval<V>) ops().create().img(input,
			Util.getTypeFromInterval(input));
	}

}
