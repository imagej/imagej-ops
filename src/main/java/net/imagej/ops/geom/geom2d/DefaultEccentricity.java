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

package net.imagej.ops.geom.geom2d;

import net.imagej.ops.Ops;
import net.imagej.ops.special.chain.RTs;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.roi.geom.real.Polygon2D;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@code geom.eccentricity}.
 * 
 * The eccentricity formula is based on "The Eccentricity of a Conic Section" by
 * Ayoub B. Ayoub.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 */
@Plugin(type = Ops.Geometric.Eccentricity.class, label = "Geometric (2D): Eccentricity")
public class DefaultEccentricity extends AbstractUnaryHybridCF<Polygon2D, DoubleType>
		implements Ops.Geometric.Eccentricity {

	private UnaryFunctionOp<Polygon2D, DoubleType> minorAxisFunc;
	private UnaryFunctionOp<Polygon2D, DoubleType> majorAxisFunc;

	@Override
	public void initialize() {
		minorAxisFunc = RTs.function(ops(), Ops.Geometric.MinorAxis.class, in());
		majorAxisFunc = RTs.function(ops(), Ops.Geometric.MajorAxis.class, in());
	}

	@Override
	public void compute(final Polygon2D input, final DoubleType output) {

		final double a = majorAxisFunc.calculate(input).get() / 2.0;
		final double b = minorAxisFunc.calculate(input).get() / 2.0;

		output.set(Math.sqrt(1 - Math.pow(b / a, 2)));
	}

	@Override
	public DoubleType createOutput(Polygon2D input) {
		return new DoubleType();
	}

}
