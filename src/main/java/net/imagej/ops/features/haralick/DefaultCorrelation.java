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
package net.imagej.ops.features.haralick;

import net.imagej.ops.Ops;
import net.imagej.ops.features.haralick.helper.CoocMeanX;
import net.imagej.ops.features.haralick.helper.CoocMeanY;
import net.imagej.ops.features.haralick.helper.CoocStdX;
import net.imagej.ops.features.haralick.helper.CoocStdY;
import net.imagej.ops.special.Functions;
import net.imagej.ops.special.UnaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * 
 * Implementation of texture correlation haralick feature.
 * 
 * @author Andreas Graumann, University of Konstanz
 * @author Christian Dietz, University of Konstanz
 *
 */
@Plugin(type = Ops.Haralick.Correlation.class, label = "Haralick: Correlation")
public class DefaultCorrelation<T extends RealType<T>> extends
		AbstractHaralickFeature<T> implements Ops.Haralick.Correlation {

	// required functions
	private UnaryFunctionOp<double[][], DoubleType> coocMeanXFunc;
	private UnaryFunctionOp<double[][], DoubleType> coocMeanYFunc;
	private UnaryFunctionOp<double[][], DoubleType> coocStdYFunc;
	private UnaryFunctionOp<double[][], DoubleType> coocStdXFunc;

	@Override
	public void initialize() {
		super.initialize();
		coocMeanXFunc = Functions.unary(ops(), CoocMeanX.class, DoubleType.class, double[][].class);
		coocMeanYFunc = Functions.unary(ops(), CoocMeanY.class, DoubleType.class, double[][].class);
		coocStdXFunc = Functions.unary(ops(), CoocStdX.class, DoubleType.class, double[][].class);
		coocStdYFunc = Functions.unary(ops(), CoocStdY.class, DoubleType.class, double[][].class);
	}
	
	@Override
	public void compute1(final IterableInterval<T> input, final DoubleType output) {
		final double[][] matrix = getCooccurrenceMatrix(input);

		final int nrGrayLevels = matrix.length;

		final double meanx = coocMeanXFunc.compute1(matrix).get();
		final double meany = coocMeanYFunc.compute1(matrix).get();
		final double stdx = coocStdXFunc.compute1(matrix).get();
		final double stdy = coocStdYFunc.compute1(matrix).get();

		double res = 0;
		for (int i = 0; i < nrGrayLevels; i++) {
			for (int j = 0; j < nrGrayLevels; j++) {
				res += ((i - meanx) * (j - meany))
						* (matrix[i][j] / (stdx * stdy));
			}
		}

		// if NaN
		if (Double.isNaN(res)) {
			output.set(0);
		} else {
			output.set(res);
		}
	}

}
