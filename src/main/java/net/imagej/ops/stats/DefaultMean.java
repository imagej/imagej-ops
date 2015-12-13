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

package net.imagej.ops.stats;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.chain.RTs;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * {@link Op} to calculate the {@code stats.max} using {@code stats.sum} and
 * {@code stats.size}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * @author Christian Dietz, University of Konstanz.
 * @param <I> input type
 * @param <O> output type
 */
@Plugin(type = Ops.Stats.Mean.class, label = "Statistics: Mean",
	priority = Priority.LOW_PRIORITY)
public class DefaultMean<I extends RealType<I>, O extends RealType<O>> extends
	AbstractStatsOp<Iterable<I>, O> implements Ops.Stats.Mean
{
	
	private UnaryFunctionOp<Iterable<I>, O> sumFunc;
	
	private UnaryFunctionOp<Iterable<I>, O> areaFunc;

	@Override
	public void initialize() {
		sumFunc = RTs.function(ops(), Ops.Stats.Sum.class, in());
		areaFunc = RTs.function(ops(), Ops.Stats.Size.class, in());
	}

	@Override
	public void compute1(final Iterable<I> input, final O output) {
		output.setReal(sumFunc.compute1(input).getRealDouble() /
			areaFunc.compute1(input).getRealDouble());
	}
}
