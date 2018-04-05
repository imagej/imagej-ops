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

package net.imagej.ops.imagemoments.hu;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.imagemoments.AbstractImageMomentOp;
import net.imagej.ops.special.chain.RTs;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@code imageMoments.huMoment5}.
 * 
 * @author Daniel Seebacher (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * @param <I> input type
 * @param <O> output type
 */
@Plugin(type = Ops.ImageMoments.HuMoment5.class, label = "Image Moment: HuMoment5")
public class DefaultHuMoment5<I extends RealType<I>, O extends RealType<O>>
	extends AbstractImageMomentOp<I, O> implements Ops.ImageMoments.HuMoment5
{

	private UnaryFunctionOp<IterableInterval<I>, O> normalizedCentralMoment30Func;

	private UnaryFunctionOp<IterableInterval<I>, O> normalizedCentralMoment12Func;

	private UnaryFunctionOp<IterableInterval<I>, O> normalizedCentralMoment21Func;

	private UnaryFunctionOp<IterableInterval<I>, O> normalizedCentralMoment03Func;

	@Override
	public void initialize() {
		normalizedCentralMoment30Func =
			RTs.function(ops(), Ops.ImageMoments.NormalizedCentralMoment30.class, in());
		normalizedCentralMoment12Func =
			RTs.function(ops(), Ops.ImageMoments.NormalizedCentralMoment12.class, in());
		normalizedCentralMoment21Func =
			RTs.function(ops(), Ops.ImageMoments.NormalizedCentralMoment21.class, in());
		normalizedCentralMoment03Func =
			RTs.function(ops(), Ops.ImageMoments.NormalizedCentralMoment03.class, in());
	}

	@Override
	public void compute(final IterableInterval<I> input, final O output) {
		final double n30 =
			normalizedCentralMoment30Func.calculate(input).getRealDouble();
		final double n12 =
			normalizedCentralMoment12Func.calculate(input).getRealDouble();
		final double n21 =
			normalizedCentralMoment21Func.calculate(input).getRealDouble();
		final double n03 =
			normalizedCentralMoment03Func.calculate(input).getRealDouble();

		output.setReal((n30 - 3 * n12) * (n30 + n12) *
			(Math.pow(n30 + n12, 2) - 3 * Math.pow(n21 + n03, 2)) + (3 * n21 - n03) *
			(n21 + n03) * (3 * Math.pow(n30 + n12, 2) - Math.pow(n21 + n03, 2)));
	}
}
