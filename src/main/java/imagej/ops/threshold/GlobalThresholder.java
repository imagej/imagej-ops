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

package imagej.ops.threshold;

import imagej.ops.Op;
import imagej.ops.OpService;
import imagej.ops.UnaryFunction;
import net.imglib2.IterableInterval;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "threshold")
public class GlobalThresholder<T extends RealType<T>> extends
	UnaryFunction<IterableInterval<T>, IterableInterval<BitType>> implements Op
{

	@Parameter
	private ThresholdMethod<T> method;

	@Parameter
	private OpService opService;

	/**
	 * Sets the thresholding method to use
	 */
	public void setMethod(final ThresholdMethod<T> method) {
		this.method = method;
	}

	@Override
	public IterableInterval<BitType> compute(final IterableInterval<T> input,
		final IterableInterval<BitType> output)
	{
		final T threshold = (T) opService.run(method, input);
		final PixThreshold<T> apply = new PixThreshold<T>();
		apply.setThreshold(threshold);
		return (IterableInterval<BitType>) opService.run("map", input, apply,
			output);
	}

	@Override
	public UnaryFunction<IterableInterval<T>, IterableInterval<BitType>> copy() {
		final GlobalThresholder<T> func = new GlobalThresholder<T>();
		func.method = method.copy();
		return func;
	}
}
