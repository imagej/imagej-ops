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

package net.imagej.ops.imagemoments.centralmoments;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Op;
import net.imagej.ops.Ops.ImageMoments.CentralMoment03;
import net.imagej.ops.Ops.ImageMoments.Moment00;
import net.imagej.ops.Ops.ImageMoments.Moment01;
import net.imagej.ops.RTs;
import net.imagej.ops.imagemoments.AbstractImageMomentOp;
import net.imagej.ops.imagemoments.ImageMomentOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@link CentralMoment03} using
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * @author Christian Dietz, University of Konstanz.
 * @param <I> input type
 * @param <O> output type
 */
@Plugin(type = ImageMomentOp.class, name = CentralMoment03.NAME,
	label = "Image Moment: CentralMoment03")
public class DefaultCentralMoment03<I extends RealType<I>, O extends RealType<O>>
	extends AbstractImageMomentOp<I, O> implements CentralMoment03
{
	
	private FunctionOp<IterableInterval<I>, O> moment00Func;
	private FunctionOp<IterableInterval<I>, O> moment01Func;

	@Override
	public void initialize() {
		moment00Func = RTs.function(ops(), Moment00.class, in());
		moment01Func = RTs.function(ops(), Moment01.class, in());
	}

	@Override
	public void compute(final IterableInterval<I> input, final O output) {
		final double moment00 = moment00Func.compute(input).getRealDouble();
		final double moment01 = moment01Func.compute(input).getRealDouble();
		
		final double centerY = moment01 / moment00;

		double centralmoment03 = 0;

		final Cursor<I> it = input.localizingCursor();
		while (it.hasNext()) {
			it.fwd();
			final double y = it.getDoublePosition(1) - centerY;
			final double val = it.get().getRealDouble();

			centralmoment03 += val * y * y * y;
		}

		output.setReal(centralmoment03);
	}
}
