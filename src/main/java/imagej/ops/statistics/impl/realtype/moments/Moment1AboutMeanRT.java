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

package imagej.ops.statistics.impl.realtype.moments;

import imagej.ops.Op;
import imagej.ops.OpService;
import imagej.ops.misc.Area;
import imagej.ops.misc.NumElements;
import imagej.ops.statistics.Mean;
import imagej.ops.statistics.impl.realtype.AbstractFunctionIRT2RT;
import imagej.ops.statistics.impl.realtype.MeanRT;
import imagej.ops.statistics.moments.Moment1AboutMean;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Christian Dietz
 * @author Andreas Graumann
 * @param <T>
 * @param <O>
 */
@Plugin(type = Op.class, name = Moment1AboutMean.NAME,
	label = Moment1AboutMean.LABEL)
public class Moment1AboutMeanRT extends AbstractFunctionIRT2RT implements
	Moment1AboutMean<Iterable<? extends RealType<?>>, RealType<?>>
{

	@Parameter
	private OpService ops;

	@Parameter(required = false)
	private MeanRT meanFunc;

	@Parameter(required = false)
	private Area<Iterable<?>, RealType<?>> areaFunc;

	@Override
	public RealType<?> compute(final Iterable<? extends RealType<?>> input,
		final RealType<?> output)
	{

		initFunctions(input);

		final double mean =
			meanFunc.compute(input, new DoubleType()).getRealDouble();

		double res = 0.0;
		for (final RealType<?> element : input) {
			res += element.getRealDouble() - mean;
		}

		output.setReal(res /
			areaFunc.compute(input, new DoubleType()).getRealDouble());

		return output;
	}

	@SuppressWarnings("unchecked")
	private void initFunctions(final Iterable<? extends RealType<?>> input) {
		if (meanFunc == null) meanFunc =
			(MeanRT) ops.op(Mean.class, DoubleType.class, input);

		if (areaFunc == null) areaFunc =
			(Area<Iterable<?>, RealType<?>>) ops.op(NumElements.class, DoubleType.class,
				input);

	}
}
