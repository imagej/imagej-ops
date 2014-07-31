/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.statistics;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.misc.Size;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.integer.LongType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Computes the mean of values for the input {@link Iterable}.
 * 
 * @author Christian Dietz
 */
@Plugin(type = Op.class, name = Mean.NAME, priority = Priority.LOW_PRIORITY)
public class MeanRealType<I extends RealType<I>, O extends RealType<O>> extends
		AbstractFunction<Iterable<I>, O> implements Mean<Iterable<I>, O> {

	@Parameter(required = false)
	private Sum<Iterable<I>, O> sumFunc;

	@Parameter(required = false)
	private Size<Iterable<I>> sizeFunc;

	@Parameter
	private OpService ops;

	@Override
	public O compute(final Iterable<I> input, final O output) {

		if (sumFunc == null) {
			sumFunc = (Sum<Iterable<I>, O>) ops.op(Sum.class, output, input);
		}
		if (sizeFunc == null) {
			sizeFunc = (Size<Iterable<I>>) ops.op(Size.class, output, input);
		}

		final O result;
		if (output == null) {
			// HACK: Need to cast through Object to satisfy javac.
			final Object o = new DoubleType();
			@SuppressWarnings("unchecked")
			final O newOutput = (O) o;
			result = newOutput;
		}
		else result = output;

		final LongType size = sizeFunc.compute(input, new LongType());
		final O sum = sumFunc.compute(input, result.copy());

		// TODO: Better way to go LongType -> O without going through double?
		result.setReal(size.getRealDouble());

		result.div(sum);

		return result;
	}
}
