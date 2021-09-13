/*-
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2021 ImageJ2 developers.
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
package net.imagej.ops.geom.geom2d;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Geometric.MinimumFeretsAngle;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.roi.geom.real.Polygon2D;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Geometric.MinimumFeretsAngle.class)
public class DefaultMinimumFeretAngle extends AbstractUnaryHybridCF<Polygon2D, DoubleType> implements MinimumFeretsAngle {

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<Polygon2D, Pair> minFeret;
	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<Pair, DoubleType> feretAngle;

	@Override
	public void initialize() {
		minFeret = Functions.unary(ops(), Ops.Geometric.MinimumFeret.class, Pair.class, in());
		feretAngle = Functions.unary(ops(), Ops.Geometric.FeretsAngle.class, DoubleType.class, Pair.class);
	}

	@Override
	public void compute(Polygon2D input, DoubleType output) {
		output.set(feretAngle.calculate(minFeret.calculate(input)).get());
	}

	@Override
	public DoubleType createOutput(Polygon2D input) {
		return new DoubleType();
	}
}
