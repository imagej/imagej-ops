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

package net.imagej.ops.image.normalize;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Converts and normalizes an {@link IterableInterval} given its minimum and
 * maximum to another range defined by minimum and maximum.
 * 
 * @author Stefan Helfrich (University of Konstanz)
 * @param <T> TODO Documentation
 * @param <O> TODO Documentation
 */
@Plugin(type = Ops.Image.Normalize.class)
public class FlexibleNormalizeIIComputer<T extends RealType<T>, O extends RealType<O>>
	extends AbstractUnaryComputerOp<IterableInterval<T>, IterableInterval<O>>
	implements Ops.Image.Normalize
{

	UnaryComputerOp<T, O> converterOp;
	UnaryComputerOp<IterableInterval<O>, IterableInterval<O>> normalizeOp;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		// TODO Replace with Ops.Convert.Copy?
		converterOp = (UnaryComputerOp) ops().op(Ops.Convert.Float64.class, out().firstElement().getClass(), in().firstElement().getClass());
		normalizeOp = (UnaryComputerOp) ops().op(Ops.Image.Normalize.class,
			out() != null ? out() : IterableInterval.class, in() != null ? in()
				: IterableInterval.class);
	}

	@Override
	public void compute(final IterableInterval<T> input,
		final IterableInterval<O> output)
	{
//		ops().run(Ops.Convert.ImageType.class, output, input, converterOp);
		IterableInterval<O> converted = ops().copy().iterableInterval(output);
		ops().map(converted, input, converterOp);

		normalizeOp.compute(converted, output);
	}
}
