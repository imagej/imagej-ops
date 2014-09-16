/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
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

package net.imagej.ops.convert;

import java.util.List;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.misc.MinMax;
import net.imagej.ops.scalepixel.ScalePixel;
import net.imagej.ops.scalepixel.ScaleUtils;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link ConvertNormalizeScale} implementation for {@link RealType}s.
 * 
 * @author Christian Dietz (University of Konstanz)
 * 
 * @param <T>
 * @param <V>
 */
@Plugin(type = Op.class, name = ConvertNormalizeScale.NAME)
public class ConvertNormalizeScaleIterableRT<T extends RealType<T>, V extends RealType<V>>
		extends AbstractFunction<Iterable<T>, Iterable<V>> implements
		ConvertNormalizeScale<Iterable<T>, Iterable<V>> {

	@Parameter
	private OpService ops;

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<V> compute(final Iterable<T> input, final Iterable<V> output) {

		final List<T> minMax = (List<T>) ops.run(MinMax.class, input);

		final V type = output.iterator().next().createVariable();

		final double oldMin = minMax.get(0).getRealDouble();
		final double oldMax = minMax.get(1).getRealDouble();

		final double newMin = type.createVariable().getMinValue();
		final double newMax = type.createVariable().getMaxValue();

		final double factor = ScaleUtils.calculateFactor(oldMin,
				oldMax, newMin, newMax);

		ops.run(ScalePixel.class, output, input, oldMin, newMin, factor);

		return output;
	}
}
