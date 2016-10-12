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

package net.imagej.ops.threshold.localBernsen;

import net.imagej.ops.Ops;
import net.imagej.ops.threshold.AbstractLocalThresholder;
import net.imagej.ops.threshold.ThresholdLearner;
import net.imagej.ops.threshold.localMidGrey.LocalMidGrey;
import net.imglib2.type.BooleanType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Local threshold method similar to {@link LocalMidGrey}, but uses a constant
 * value rather than the value of the input pixel when the contrast in the
 * neighborhood of that pixel is too small.
 *
 * @author Jonathan Hale
 * @author Stefan Helfrich (University of Konstanz)
 * @param <I> type of input
 * @param <O> type of output
 */
@Plugin(type = Ops.Threshold.LocalBernsenThreshold.class,
	priority = Priority.LOW_PRIORITY)
public class LocalBernsen<I, O extends BooleanType<O>> extends
	AbstractLocalThresholder<I, O> implements
	Ops.Threshold.LocalBernsenThreshold
{

	@Parameter
	private double constrastThreshold;

	@Parameter
	private double halfMaxValue;

	@SuppressWarnings("unchecked")
	@Override
	protected ThresholdLearner<I, O> getLearner() {
		return ops().op(LocalBernsenThresholdLearner.class, in(),
			constrastThreshold, halfMaxValue);
	}

}
