/*
 * #%L
 * ImageJ OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2014 Board of Regents of the University of
 * Wisconsin-Madison and University of Konstanz.
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

package net.imagej.ops.features.firstorder;

import net.imagej.ops.Op;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.Moment3AboutMeanFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.SkewnessFeature;
import net.imagej.ops.features.firstorder.FirstOrderFeatures.StdDeviationFeature;
import net.imagej.ops.statistics.FirstOrderOps.Skewness;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Skewness}. Use {@link FeatureService} to
 * compile this {@link Op}.
 * 
 * @author Christian Dietz
 * @author Andreas Graumann
 */
@Plugin(type = Op.class, name = Skewness.NAME, priority = Priority.VERY_HIGH_PRIORITY)
public class DefaultSkewnessFeature<T extends RealType<T>, O extends RealType<O>>
		implements SkewnessFeature<O> {

	@Parameter
	private Moment3AboutMeanFeature<O> moment3;

	@Parameter
	private StdDeviationFeature<O> stdDev;

	@Parameter(type = ItemIO.OUTPUT)
	private O out;

	@Override
	public void run() {

		if (out == null)
			out = moment3.getOutput().createVariable();

		final double moment3 = this.moment3.getOutput().getRealDouble();
		final double std = this.stdDev.getOutput().getRealDouble();

		if (std != 0) {
			out.setReal((moment3) / (std * std * std));
		} else {
			out.setReal(Double.NaN);
		}
	}

	@Override
	public O getOutput() {
		return out;
	}

	@Override
	public void setOutput(O output) {
		out = output;
	}

}
