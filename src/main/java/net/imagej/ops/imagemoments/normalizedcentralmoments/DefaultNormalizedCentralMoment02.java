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

package net.imagej.ops.imagemoments.normalizedcentralmoments;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.imagemoments.AbstractImageMomentOp;
import net.imagej.ops.special.chain.RTs;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@code imageMoments.normalizedCentralMoment02}.
 * 
 * @author Daniel Seebacher (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * @param <I> input type
 * @param <O> output type
 */
@Plugin(type = Ops.ImageMoments.NormalizedCentralMoment02.class,
	label = "Image Moment: NormalizedCentralMoment02")
public class DefaultNormalizedCentralMoment02<I extends RealType<I>, O extends RealType<O>>
	extends AbstractImageMomentOp<I, O> implements Ops.ImageMoments.NormalizedCentralMoment02
{

	private UnaryFunctionOp<IterableInterval<I>, O> centralMoment00Func;

	private UnaryFunctionOp<IterableInterval<I>, O> centralMoment02Func;

	@Override
	public void initialize() {
		centralMoment00Func = RTs.function(ops(), Ops.ImageMoments.CentralMoment00.class, in());
		centralMoment02Func = RTs.function(ops(), Ops.ImageMoments.CentralMoment02.class, in());
	}

	@Override
	public void compute(final IterableInterval<I> input, final O output) {
		double centralMoment00 = centralMoment00Func.calculate(input).getRealDouble();
		double centralMoment02 = centralMoment02Func.calculate(input).getRealDouble();

		output.setReal(centralMoment02 /
			Math.pow(centralMoment00, 1 + ((0 + 2) / 2.0)));
	}
}
