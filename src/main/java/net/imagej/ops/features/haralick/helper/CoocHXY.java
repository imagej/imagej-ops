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
package net.imagej.ops.features.haralick.helper;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.OpService;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * NB: Helper class. Internal usage only.
 * 
 * @author Andreas Graumann, University of Konstanz
 * @author Christian Dietz, University of Konstanz
 */
@Plugin(type = CoocHXY.class)
public class CoocHXY extends AbstractFunctionOp<double[][], double[]> {

	private static final double EPSILON = 0.00000001f;

	@Override
	public double[] compute(double[][] matrix) {
		double hx = 0.0d;
		double hy = 0.0d;
		double hxy1 = 0.0d;
		double hxy2 = 0.0d;

		final int nrGrayLevels = matrix.length;

		final double[] px = (double[]) ops().run(CoocPX.class, (Object) matrix);
		final double[] py = (double[]) ops().run(CoocPY.class, (Object) matrix);

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
