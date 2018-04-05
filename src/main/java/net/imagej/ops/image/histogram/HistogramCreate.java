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

package net.imagej.ops.image.histogram;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.histogram.Real1dBinMapper;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Martin Horn (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 */
@Plugin(type = Ops.Image.Histogram.class)
public class HistogramCreate<T extends RealType<T>> extends
		AbstractUnaryFunctionOp<Iterable<T>, Histogram1d<T>> implements
		Ops.Image.Histogram {

	@Parameter(required = false)
	private int numBins = 256;

	private UnaryFunctionOp<Iterable<T>, Pair<T, T>> minMaxFunc;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		minMaxFunc = (UnaryFunctionOp) Functions.unary(ops(), Ops.Stats.MinMax.class, Pair.class,
				in() != null ? in() : Iterable.class);
	}

	@Override
	public Histogram1d<T> calculate(final Iterable<T> input) {
		final Pair<T, T> res = minMaxFunc.calculate(input);

		final Histogram1d<T> histogram1d = new Histogram1d<>(
				new Real1dBinMapper<T>(res.getA().getRealDouble(), res.getB()
						.getRealDouble(), numBins, false));

		histogram1d.countData(input);

		return histogram1d;
	}
}
