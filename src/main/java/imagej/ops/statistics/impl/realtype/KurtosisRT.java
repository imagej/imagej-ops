/*
 * #%L
 * SciJava OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2013 - 2014 Board of Regents of the University of
 * Wisconsin-Madison, and University of Konstanz.
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

package imagej.ops.statistics.impl.realtype;

import imagej.ops.Op;
import imagej.ops.OpService;
import imagej.ops.statistics.Kurtosis;
import imagej.ops.statistics.impl.realtype.moments.Moment4AboutMeanRT;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Kurtosis.NAME, label = Kurtosis.LABEL,
	priority = Priority.LOW_PRIORITY)
public class KurtosisRT extends AbstractFunctionIRT2RT implements
	Kurtosis<Iterable<? extends RealType<?>>, RealType<?>>
{

	@Parameter
	private OpService ops;

	@Parameter(required = false)
	private StdDeviationRT stddev;

	@Parameter(required = false)
	private Moment4AboutMeanRT moment4;

	// TODO: Remove?
	private DoubleType tmp = new DoubleType();

	@Override
	public RealType<?> compute(final Iterable<? extends RealType<?>> input,
		final RealType<?> output)
	{
		initFunctions(input);

		final double std = this.stddev.compute(input, tmp).getRealDouble();
		final double moment4 = this.moment4.compute(input, tmp).getRealDouble();

		if (std != 0) {
			output.setReal((moment4) / (std * std * std * std));
		}
		else {
			output.setReal(0.0d);
		}

		return output;
	}

	private void initFunctions(final Iterable<? extends RealType<?>> input) {
		if (stddev == null) stddev =
			(StdDeviationRT) ops.op(StdDeviationRT.class, DoubleType.class, input);

		if (moment4 == null) moment4 =
			(Moment4AboutMeanRT) ops.op(Moment4AboutMeanRT.class, DoubleType.class,
				input);
	}
}
