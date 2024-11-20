/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2016 Board of Regents of the University of
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
package net.imagej.ops.features.sets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.imagej.ops.Op;
import net.imagej.ops.features.sets.processors.ComputerSetProcessorUtils;
import net.imagej.ops.image.histogram.HistogramCreate;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.histogram.Histogram1d;
import net.imglib2.type.numeric.integer.LongType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Default implementation of {@link HistogramComputerSet}.
 *
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
@Plugin(type = ComputerSet.class, label = "Histogram Computerset")
@SuppressWarnings("rawtypes")
public class DefaultHistogramComputerSet extends AbstractComputerSet<Iterable, LongType>
		implements HistogramComputerSet<Iterable, LongType> {

	@Parameter(type = ItemIO.INPUT, label = "Number of Bins", description = "The number of bins of the histogram", min = "1", max = "2147483647", stepSize = "1")
	private int numBins = 256;

	private UnaryFunctionOp<Iterable, Histogram1d> histogramFunc;

	public DefaultHistogramComputerSet() {
		super(new LongType(), Iterable.class);
	}

	@Override
	public void initialize() {
		super.initialize();
		histogramFunc = Functions.unary(ops(), HistogramCreate.class, Histogram1d.class, in(), numBins);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Op>[] getComputers() {
		return new Class[] { HistogramCreate.class };
	}

	@Override
	protected Iterable getFakeInput() {
		return ComputerSetProcessorUtils.getIterable();
	}

	@Override
	protected void addComputer(final Class<? extends Op> clazz) {
		for (int i = 0; i < numBins; i++) {
			final LongType output = outputTypeInstance.createVariable();
			namedOutputs.put(getKey(i), output);
		}
	}

	@Override
	public Map<String, LongType> calculate(final Iterable input) {
		final Histogram1d histogram = histogramFunc.calculate(input);
		final Map<String, LongType> result = new HashMap<>();
		for (int i = 0; i < numBins; i++) {
			result.put(getKey(i), new LongType(histogram.frequency(i)));
		}
		return result;
	}

	private String getKey(final int bin) {
		return "Histogram Bin[" + bin + "]";
	}

	@Override
	public String[] getComputerNames() {
		final List<String> names = new ArrayList<>();
		for (int i = 0; i < numBins; i++) {
			names.add(getKey(i));
		}
		return names.toArray(new String[names.size()]);
	}
}
