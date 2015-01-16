/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2014 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
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

package net.imagej.threshold;

import net.imglib2.histogram.Histogram1d;

import org.scijava.plugin.Plugin;

// NB - this plugin adapted from Gabriel Landini's code of his AutoThreshold
// plugin found in Fiji (version 1.14). The method was ported from IJ1 by
// Gabriel and somewhat enhanced ("re-implemented so we can ignore black/white
// and set the bright or dark objects")

/**
 * Implements the default threshold method from ImageJ 1.x.
 * 
 * @author Barry DeZonia
 * @author Gabriel Landini
 * @deprecated Use {@link net.imagej.ops.threshold} instead.
 */
@Deprecated
@Plugin(type = ThresholdMethod.class, name = "Default")
public class DefaultThresholdMethod extends AbstractThresholdMethod {

	@Override
	public long getThreshold(Histogram1d<?> hist) {
		long[] histogram = hist.toLongArray();
		// Original IJ implementation for compatibility.
		int level;
		int maxValue = histogram.length - 1;
		double result, sum1, sum2, sum3, sum4;

		int min = 0;
		while (histogram[min] == 0 && min < maxValue)
			min++;
		int max = maxValue;
		while (histogram[max] == 0 && max > 0)
			max--;
		if (min >= max) {
			level = histogram.length / 2;
			return level;
		}

		int movingIndex = min;
		do {
			sum1 = sum2 = sum3 = sum4 = 0.0;
			for (int i = min; i <= movingIndex; i++) {
				sum1 += i * histogram[i];
				sum2 += histogram[i];
			}
			for (int i = movingIndex + 1; i <= max; i++) {
				sum3 += i * histogram[i];
				sum4 += histogram[i];
			}
			result = (sum1 / sum2 + sum3 / sum4) / 2.0;
			movingIndex++;
		}
		while (movingIndex + 1 <= result && movingIndex < max - 1);

		level = (int) Math.round(result);
		return level;
	}

	@Override
	public String getMessage() {
		return null;
	}

}
