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
package net.imagej.ops.features.tamura2d;

import net.imagej.ops.Ops;
import net.imagej.ops.special.chain.RTs;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Default implementation of tamura feature contrast.
 * 
 * @author Andreas Graumann (University of Konstanz)
 */
@Plugin(type = Ops.Tamura.Contrast.class, label = "Tamura 2D: Contrast")
public class DefaultContrastFeature<I extends RealType<I>, O extends RealType<O>>
		extends AbstractTamuraFeature<I, O> implements Ops.Tamura.Contrast {

	private UnaryFunctionOp<RandomAccessibleInterval<I>, O> m4Op;
	private UnaryFunctionOp<RandomAccessibleInterval<I>, O> varOp;
	private UnaryFunctionOp<RandomAccessibleInterval<I>, O> stdOp;

	@Override
	public void initialize() {
		m4Op = RTs.function(ops(), Ops.Stats.Moment4AboutMean.class, in());
		varOp = RTs.function(ops(), Ops.Stats.Variance.class, in());
		stdOp = RTs.function(ops(), Ops.Stats.StdDev.class, in());
	}

	@Override
	public void compute(final RandomAccessibleInterval<I> input, final O output) {

		// Get fourth moment about mean
		double m4 = m4Op.calculate(input).getRealDouble();
		double var = varOp.calculate(input).getRealDouble();
		double std = stdOp.calculate(input).getRealDouble();

		double l4 = m4 / (var * var);

		// contrast
		double fCon = std / Math.pow(l4, 0.25);
		output.setReal(fCon);
	}

}
