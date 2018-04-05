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

package net.imagej.ops.geom;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.type.numeric.real.DoubleType;

/**
 * Generic implementation of {@link net.imagej.ops.Ops.Geometric.Solidity}.
 * 
 * Based on https://de.mathworks.com/help/images/ref/regionprops.html.
 * 
 * @author Tim-Oliver Buchholz (University of Konstanz)
 */
public abstract class AbstractSolidity<I> extends AbstractUnaryHybridCF<I, DoubleType>
		implements Ops.Geometric.Solidity {

	private UnaryFunctionOp<I, DoubleType> volume;

	private UnaryFunctionOp<I, DoubleType> convexHullVolume;

	@Override
	public void initialize() {
		volume = Functions.unary(ops(), Ops.Geometric.Size.class, DoubleType.class, in());
		convexHullVolume = Functions.unary(ops(), Ops.Geometric.SizeConvexHull.class, DoubleType.class, in());
	}

	@Override
	public void compute(final I input, final DoubleType output) {
		output.set(volume.calculate(input).get() / convexHullVolume.calculate(input).get());
	}

	@Override
	public DoubleType createOutput(I input) {
		return new DoubleType();
	}

}
