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

package net.imagej.ops.features.haralick.helper;

import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;

import org.scijava.plugin.Plugin;

/**
 * NB: Helper class. Internal usage only.
 * 
 * @author Andreas Graumann (University of Konstanz)
 * @author Christian Dietz (University of Konstanz)
 * @author Tim-Oliver Buchholz (University of Konstanz)
 */
@Plugin(type = CoocHXY.class)
public class CoocHXY extends AbstractUnaryFunctionOp<double[][], double[]> {

	private static final double EPSILON = Double.MIN_NORMAL;

	private UnaryFunctionOp<double[][], double[]> coocPXFunc;
	private UnaryFunctionOp<double[][], double[]> coocPYFunc;

	@Override
	public void initialize() {
		super.initialize();

		coocPXFunc = Functions.unary(ops(), CoocPX.class, double[].class, double[][].class);
		coocPYFunc = Functions.unary(ops(), CoocPY.class, double[].class, double[][].class);
	}

	@Override
	public double[] calculate(double[][] matrix) {
		double hx = 0.0d;
		double hy = 0.0d;
		double hxy1 = 0.0d;
		double hxy2 = 0.0d;

		final int nrGrayLevels = matrix.length;

		final double[] px = coocPXFunc.calculate(matrix);
		final double[] py = coocPYFunc.calculate(matrix);

		for (int i = 0; i < px.length; i++) {
			hx += px[i] * Math.log(px[i] + EPSILON);
		}
		hx = -hx;

		for (int j = 0; j < py.length; j++) {
			hy += py[j] * Math.log(py[j] + EPSILON);
		}
		hy = -hy;
		for (int i = 0; i < nrGrayLevels; i++) {
			for (int j = 0; j < nrGrayLevels; j++) {
				hxy1 += matrix[i][j] * Math.log(px[i] * py[j] + EPSILON);
				hxy2 += px[i] * py[j] * Math.log(px[i] * py[j] + EPSILON);
			}
		}
		hxy1 = -hxy1;
		hxy2 = -hxy2;

		return new double[] { hx, hy, hxy1, hxy2 };
	}
}
