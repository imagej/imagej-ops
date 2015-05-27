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

package net.imagej.ops.statistics.firstorder;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpUtils;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.MinFeature;
import net.imagej.ops.statistics.FirstOrderOps.Min;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * Calculate {@link Min} of {@link Iterable} of {@link RealType}.
 * 
 * @author Christian Dietz
 */
@Plugin(type = Op.class, name = Min.NAME, label = Min.LABEL, priority = Priority.LOW_PRIORITY)
public class DefaultMinIRT<I extends RealType<I>, O extends RealType<O>>
		extends AbstractOutputFunction<Iterable<I>, O> implements Min,
		MinFeature<O> {

	@Override
	public O createOutput(Iterable<I> in) {
        return OpUtils.<O> cast(new DoubleType());
	}

	@Override
	protected O safeCompute(final Iterable<I> input, final O output) {

		double min = Double.POSITIVE_INFINITY;

		for (final RealType<?> val : input) {
			final double tmp = val.getRealDouble();
			if (tmp < min) {
				min = tmp;
			}
		}

		output.setReal(min);
		return output;
	}
}
