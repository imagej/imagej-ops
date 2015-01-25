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
import net.imagej.ops.features.haralick.HaralickFeatures.SumAverageFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.SumVarianceFeature;
import net.imagej.ops.features.haralick.helper.CoocPXPlusY;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = SumVarianceFeature.NAME)
public class DefaultSumVarianceFeature implements
		SumVarianceFeature {

	@Parameter
	private SumAverageFeature sumAverage;

	@Parameter
	private CoocPXPlusY coocPXPlusY;

	@Parameter
	private CooccurrenceMatrix matrix;

	@Parameter(type = ItemIO.OUTPUT)
	private double output;

	@Override
	public double getFeatureValue() {
		return output;
	}

	@Override
	public void run() {
		final double[] pxplusy = coocPXPlusY.getOutput();
		final int nrGrayLevels = matrix.getOutput().length;
		final double average = sumAverage.getFeatureValue();

		output = 0;
		for (int i = 2; i <= 2 * nrGrayLevels; i++) {
			output += (i - average) * (i - average) * pxplusy[i];
		}
	}
}
