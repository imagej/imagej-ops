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

package net.imagej.ops.normalize;

import java.util.List;

import net.imagej.ops.AbstractStrictFunction;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.statistics.FirstOrderOps.MinMax;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Ops.Normalize.NAME, attrs = { @Attr(
	name = "aliases", value = Ops.Normalize.ALIASES) })
public class NormalizeIterableInterval<T extends RealType<T>> extends
	AbstractStrictFunction<IterableInterval<T>, IterableInterval<T>> implements
	Ops.Normalize
{

	@Parameter
	private OpService ops;

	@Override
	public IterableInterval<T> compute(IterableInterval<T> input,
		IterableInterval<T> output)
	{

		T outType = output.firstElement().createVariable();
		List<T> minmax = (List<T>) ops.run(MinMax.class, input);
		double factor =
			NormalizeRealType.normalizationFactor(minmax.get(0).getRealDouble(),
				minmax.get(1).getRealDouble(), outType.getMinValue(), outType
					.getMaxValue());

		// lookup the pixel-wise normalize function
		Op normalize =
			ops.op(Ops.Normalize.class, outType, outType, minmax.get(0).getRealDouble(), outType.getMinValue(), outType
					.getMaxValue(), factor);

		// run normalize for each pixel
		ops.run("map", output, input, normalize);

		return output;
	}

}
