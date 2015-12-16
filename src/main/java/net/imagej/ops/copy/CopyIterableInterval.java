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

package net.imagej.ops.copy;

import net.imagej.ops.Contingent;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.AbstractUnaryHybridOp;
import net.imagej.ops.special.Computers;
import net.imagej.ops.special.UnaryComputerOp;
import net.imglib2.IterableInterval;
import net.imglib2.util.Intervals;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Copies an {@link IterableInterval} into another {@link IterableInterval}
 * 
 * @author Christian Dietz, University of Konstanz
 * @param <T>
 */
@Plugin(type = Ops.Copy.IterableInterval.class, priority = 1.0)
public class CopyIterableInterval<T> extends
		AbstractUnaryHybridOp<IterableInterval<T>, IterableInterval<T>> implements
		Ops.Copy.IterableInterval, Contingent {

	@Parameter
	protected OpService ops;

	// used internally
	private UnaryComputerOp<IterableInterval<T>, IterableInterval<T>> map;
	
	@Override
	public void initialize() {
		map = Computers.unary(ops, Ops.Map.class, in(), in(),
				Computers.unary(ops, Ops.Copy.Type.class, 
						in().firstElement().getClass(), 
						in().firstElement().getClass()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public IterableInterval<T> createOutput(final IterableInterval<T> input) {
		// FIXME: Assumption here: Create an Img. I would rather like: Create
		// what ever is best given the input.
		return (IterableInterval<T>) ops.create().img(input);
	}

	@Override
	public void compute1(final IterableInterval<T> input,
			final IterableInterval<T> output) {
		map.compute1(input, output);
	}

	@Override
	public boolean conforms() {
		if (out() != null) {
			return Intervals.equalDimensions(in(), out());
		}
		return true;
	}
}
