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

package net.imagej.ops.image.integral;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.integral.IntegralImg;
import net.imglib2.converter.RealDoubleConverter;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Wrapper op for the creation of integral images with
 * {@code net.imglib2.algorithm.integral.IntegralImg}.
 *
 * @see IntegralImg
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Image.Integral.class, priority = Priority.LOW)
public class WrappedIntegralImg<I extends RealType<I>> extends
	AbstractUnaryFunctionOp<RandomAccessibleInterval<I>, RandomAccessibleInterval<DoubleType>>
	implements Ops.Image.Integral
{

	private IntegralImg<I, DoubleType> integralImg;

	@Override
	public RandomAccessibleInterval<DoubleType> calculate(
		final RandomAccessibleInterval<I> input)
	{
		// Create IntegralImg from input
		integralImg = new IntegralImg<>(input, new DoubleType(),
			new RealDoubleConverter<I>());

		// integralImg will be larger by one pixel in each dimension than input
		// due
		// to the computation of the integral image
		RandomAccessibleInterval<DoubleType> img = null;
		if (integralImg.process()) {
			img = integralImg.getResult();
		}

		return img;
	}

}
