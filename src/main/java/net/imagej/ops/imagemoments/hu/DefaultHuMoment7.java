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

package net.imagej.ops.imagemoments.hu;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.ImageMoments.HuMoment7;
import net.imagej.ops.Ops.ImageMoments.NormalizedCentralMoment03;
import net.imagej.ops.Ops.ImageMoments.NormalizedCentralMoment12;
import net.imagej.ops.Ops.ImageMoments.NormalizedCentralMoment21;
import net.imagej.ops.Ops.ImageMoments.NormalizedCentralMoment30;
import net.imagej.ops.RTs;
import net.imagej.ops.imagemoments.AbstractImageMomentOp;
import net.imagej.ops.imagemoments.ImageMomentOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@link HuMoment7}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * @author Christian Dietz, University of Konstanz.
 * @param <I> input type
 * @param <O> output type
 */
@Plugin(type = ImageMomentOp.class, name = HuMoment7.NAME,
	label = "Image Moment: HuMoment7")
public class DefaultHuMoment7<I extends RealType<I>, O extends RealType<O>>
	extends AbstractImageMomentOp<I, O> implements HuMoment7
{

	private FunctionOp<IterableInterval<I>, O> normalizedCentralMoment30Func;

	private FunctionOp<IterableInterval<I>, O> normalizedCentralMoment12Func;

	private FunctionOp<IterableInterval<I>, O> normalizedCentralMoment21Func;

	private FunctionOp<IterableInterval<I>, O> normalizedCentralMoment03Func;

	@Override
	public void initialize() {
		normalizedCentralMoment03Func =
			RTs.function(ops(), NormalizedCentralMoment03.class, in());
		normalizedCentralMoment12Func =
			RTs.function(ops(), NormalizedCentralMoment12.class, in());
		normalizedCentralMoment21Func =
			RTs.function(ops(), NormalizedCentralMoment21.class, in());
		normalizedCentralMoment30Func =
			RTs.function(ops(), NormalizedCentralMoment30.class, in());
	}

	@Override
	public void compute(final IterableInterval<I> input, final O output) {
		double n03 = normalizedCentralMoment03Func.compute(input).getRealDouble();
		double n12 = normalizedCentralMoment12Func.compute(input).getRealDouble();
		double n21 = normalizedCentralMoment21Func.compute(input).getRealDouble();
		double n30 = normalizedCentralMoment30Func.compute(input).getRealDouble();

		output.setReal((3 * n21 - n03) * (n30 + n12) *
			(Math.pow(n30 + n12, 2) - 3 * Math.pow(n21 + n03, 2)) - (n30 - 3 * n12) *
			(n21 + n03) * (3 * Math.pow(n30 + n12, 2) - Math.pow(n21 + n03, 2)));
	}
}
