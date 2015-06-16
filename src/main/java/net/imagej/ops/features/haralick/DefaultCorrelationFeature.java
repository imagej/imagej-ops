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
package net.imagej.ops.features.haralick;

import net.imagej.ops.Op;
import net.imagej.ops.features.haralick.HaralickFeatures.CorrelationFeature;
import net.imagej.ops.features.haralick.helper.CoocMeanX;
import net.imagej.ops.features.haralick.helper.CoocMeanY;
import net.imagej.ops.features.haralick.helper.CoocStdX;
import net.imagej.ops.features.haralick.helper.CoocStdY;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, label = "Haralick 2D: Correlation")
public class DefaultCorrelationFeature implements
		CorrelationFeature<DoubleType> {

	@Parameter
	private CooccurrenceMatrix cooc;

	@Parameter
	private CoocMeanX coocMeanX;

	@Parameter
	private CoocMeanY coocMeanY;

	@Parameter
	private CoocStdX coocStdX;

	@Parameter
	private CoocStdY coocStdY;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;

	@Override
	public void run() {

		if (out == null)
			out = new DoubleType();

		final double[][] matrix = cooc.getOutput();
		final int nrGrayLevels = matrix.length;
		final double meanx = coocMeanX.getOutput().get();
		final double meany = coocMeanY.getOutput().get();
		final double stdx = coocStdX.getOutput().get();
		final double stdy = coocStdY.getOutput().get();

		double output = 0;
		for (int i = 0; i < nrGrayLevels; i++) {
			for (int j = 0; j < nrGrayLevels; j++) {
				output += ((i - meanx) * (j - meany)) * matrix[i][j]
						/ (stdx * stdy);
			}
		}

		// if NaN
		if (Double.isNaN(output)) {
			out.set(0);
		} else {
			out.setReal(output);
		}
	}

	@Override
	public DoubleType getOutput() {
		return out;
	}

	@Override
	public void setOutput(DoubleType output) {
		out = output;
	}
}
