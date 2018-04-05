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

package net.imagej.ops.copy;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.IterableInterval;
import net.imglib2.util.Intervals;

import org.scijava.plugin.Plugin;

/**
 * Copies an {@link IterableInterval} into another {@link IterableInterval}
 * 
 * @author Christian Dietz (University of Konstanz)
 * @param <T>
 */
@Plugin(type = Ops.Copy.IterableInterval.class, priority = 1.0)
public class CopyII<T> extends
		AbstractUnaryHybridCF<IterableInterval<T>, IterableInterval<T>> implements
		Ops.Copy.IterableInterval, Contingent {

	// used internally
	private UnaryComputerOp<IterableInterval<T>, IterableInterval<T>> map;
	private UnaryFunctionOp<IterableInterval<T>, IterableInterval<T>> imgCreator;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		map = Computers.unary(ops(), Ops.Map.class, in(), in(), Computers.unary(
			ops(), Ops.Copy.Type.class, in().firstElement().getClass(), in()
				.firstElement().getClass()));
		imgCreator = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			IterableInterval.class, in(), in().firstElement());
	}

	@Override
	public IterableInterval<T> createOutput(final IterableInterval<T> input) {
		// FIXME: Assumption here: Create an Img. I would rather like: Create
		// what ever is best given the input.
		return imgCreator.calculate(input);
	}

	@Override
	public void compute(final IterableInterval<T> input,
			final IterableInterval<T> output) {
		map.compute(input, output);
	}

	@Override
	public boolean conforms() {
		if (out() != null) {
			return Intervals.equalDimensions(in(), out());
		}
		return true;
	}
}
