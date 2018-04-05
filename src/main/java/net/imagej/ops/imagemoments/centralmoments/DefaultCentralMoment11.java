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

package net.imagej.ops.imagemoments.centralmoments;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.imagemoments.AbstractImageMomentOp;
import net.imagej.ops.special.chain.RTs;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@code imageMoments.centralMoment11}.
 * 
 * @author Daniel Seebacher (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * @param <I> input type
 * @param <O> output type
 */
@Plugin(type = Ops.ImageMoments.CentralMoment11.class, label = "Image Moment: CentralMoment11")
public class DefaultCentralMoment11<I extends RealType<I>, O extends RealType<O>>
	extends AbstractImageMomentOp<I, O> implements Ops.ImageMoments.CentralMoment11
{

	private UnaryFunctionOp<IterableInterval<I>, O> moment00Func;

	private UnaryFunctionOp<IterableInterval<I>, O> moment01Func;

	private UnaryFunctionOp<IterableInterval<I>, O> moment10Func;

	private UnaryFunctionOp<IterableInterval<I>, O> moment11Func;

	@Override
	public void initialize() {
		moment00Func = RTs.function(ops(), Ops.ImageMoments.Moment00.class, in());
		moment01Func = RTs.function(ops(), Ops.ImageMoments.Moment01.class, in());
		moment10Func = RTs.function(ops(), Ops.ImageMoments.Moment10.class, in());
		moment11Func = RTs.function(ops(), Ops.ImageMoments.Moment11.class, in());
	}

	@Override
	public void compute(final IterableInterval<I> input, final O output) {
		final double moment00 = moment00Func.calculate(input).getRealDouble();
		final double moment01 = moment01Func.calculate(input).getRealDouble();
		final double moment10 = moment10Func.calculate(input).getRealDouble();
		final double moment11 = moment11Func.calculate(input).getRealDouble();

		final double centerX = moment10 / moment00;

		output.setReal(moment11 - (centerX * moment01));
	}
}
