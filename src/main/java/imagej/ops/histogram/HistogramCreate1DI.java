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

package imagej.ops.histogram;

import imagej.ops.Op;
import imagej.ops.OpService;
import imagej.ops.descriptors.statistics.MinMax;

import java.util.List;

import net.imglib2.histogram.Histogram1d;
import net.imglib2.histogram.Real1dBinMapper;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Martin Horn, University of Konstanz
 */
@Plugin(type = Op.class, name = HistogramCreate1D.NAME,
	label = HistogramCreate1D.LABEL)
public class HistogramCreate1DI<T extends RealType<T>> implements
	HistogramCreate1D<T>
{

	@Parameter
	private Iterable<T> in;

	@Parameter(required = false)
	private int numBins = 256;

	@Parameter(type = ItemIO.OUTPUT)
	private Histogram1d<T> out;

	@Parameter
	private OpService ops;

	@Override
	public void run() {
		@SuppressWarnings("unchecked")
		final List<T> res = (List<T>) ops.run(MinMax.class, in);
		out =
			new Histogram1d<T>(new Real1dBinMapper<T>(res.get(0).getRealDouble(), res
				.get(1).getRealDouble(), numBins, false));

	}

	@Override
	public Histogram1d<T> getHistogram() {
		return out;
	}
}
