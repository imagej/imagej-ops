/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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
package net.imagej.ops.features.haralick;

import net.imagej.ops.Ops;
import net.imagej.ops.features.haralick.helper.CoocPXMinusY;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * 
 * Implementation of Difference Variance Haralick Feature
 * 
 * @author Andreas Graumann (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * @author Tim-Oliver Buchholz (University of Konstanz)
 *
 * Formula based on: http://haralick.org/journals/TexturalFeatures.pdf
 */
@Plugin(type = Ops.Haralick.DifferenceVariance.class, label = "Haralick: Difference Variance")
public class DefaultDifferenceVariance<T extends RealType<T>> extends AbstractHaralickFeature<T>
		implements Ops.Haralick.DifferenceVariance {

	private UnaryFunctionOp<double[][], double[]> coocPXMinusYFunc;

	@Override
	public void initialize() {
		super.initialize();
		coocPXMinusYFunc = Functions.unary(ops(), CoocPXMinusY.class, double[].class, double[][].class);
	}

	@Override
	public void compute(final IterableInterval<T> input, final DoubleType output) {
		final double[][] matrix = getCooccurrenceMatrix(input);
		final double[] pxminusy = coocPXMinusYFunc.calculate(matrix);

		double mu = 0.0;

		for (int i = 0; i < numGreyLevels; i++) {
			mu += i * pxminusy[i];
		}

		double sum = 0.0d;
		for (int k = 0; k < numGreyLevels; k++) {
			sum += Math.pow(k - mu, 2) * pxminusy[k];
		}

		output.set(sum);
	}

}
