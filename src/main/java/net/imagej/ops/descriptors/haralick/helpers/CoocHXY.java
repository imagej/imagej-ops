/*
 * #%L
 * SciJava OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2013 Board of Regents of the University of
 * Wisconsin-Madison, and University of Konstanz.
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
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */
package net.imagej.ops.descriptors.haralick.helpers;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imglib2.ops.data.CooccurrenceMatrix;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class)
public class CoocHXY implements OutputOp<double[]> {

	private static final double EPSILON = 0.00000001f;

	@Parameter
	private CoocParameter param;

	@Parameter
	private CooccurrenceMatrix matrix;

	@Parameter
	private CoocPX coocPX;

	@Parameter
	private CoocPY coocPY;

	@Parameter(type = ItemIO.OUTPUT)
	private double[] output;

	@Override
	public void run() {
		double hx = 0.0d;
		double hy = 0.0d;
		double hxy1 = 0.0d;
		double hxy2 = 0.0d;

		final int nrGrayLevels = param.getNrGrayLevels();
		final double[] px = coocPX.getOutput();
		final double[] py = coocPY.getOutput();

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
				hxy1 += matrix.getValueAt(i, j)
						* Math.log(px[i] * py[j] + EPSILON);
				hxy2 += px[i] * py[j] * Math.log(px[i] * py[j] + EPSILON);
			}
		}
		hxy1 = -hxy1;
		hxy2 = -hxy2;

		output = new double[] { hx, hy, hxy1, hxy2 };
	}

	@Override
	public double[] getOutput() {
		return output;
	}
}
