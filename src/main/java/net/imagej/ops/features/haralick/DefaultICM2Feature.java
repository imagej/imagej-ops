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
import net.imagej.ops.features.haralick.HaralickFeatures.EntropyFeature;
import net.imagej.ops.features.haralick.HaralickFeatures.ICM2Feature;
import net.imagej.ops.features.haralick.helper.CoocHXY;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Implementation of Information Measure of Correlation 2 Haralick Feature
 * 
 * @author Andreas Graumann, University of Konstanz
 * @author Christian Dietz, University of Konstanz
 *
 */
@Plugin(type = Op.class, label = "Haralick: ICM2", name = "Haralick: Information Measure of Correlation 2")
public class DefaultICM2Feature implements ICM2Feature<DoubleType> {

	@Parameter
	private EntropyFeature<DoubleType> entropy;

	@Parameter
	private CoocHXY coocHXY;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType output;

	@Override
	public void run() {
		
		if (output == null) {
			output = new DoubleType();
		}
		
		double res = 0;
		final double[] coochxy = coocHXY.getOutput();
		res = Math.sqrt(1 - Math.exp(-2
				* (coochxy[3] - entropy.getOutput().get())));
		
		// if NaN
		if (Double.isNaN(res)) {
			output.set(0);
		} else {
			output.set(res);
		}
		
	}

	@Override
	public DoubleType getOutput() {
		return output;
	}

	@Override
	public void setOutput(DoubleType _output) {
		output = _output;
	}

}
