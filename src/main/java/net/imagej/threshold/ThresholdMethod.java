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

import net.imagej.ImageJPlugin;
import net.imglib2.histogram.Histogram1d;

import org.scijava.plugin.RichPlugin;
import org.scijava.plugin.SingletonPlugin;

/**
 * An algorithm for thresholding an image into two classes of pixels from its
 * histogram.
 * 
 * @author Barry DeZonia
 * @deprecated Use {@link net.imagej.ops.threshold} instead.
 */
@Deprecated
public interface ThresholdMethod extends ImageJPlugin, RichPlugin,
	SingletonPlugin
{

	/**
	 * Calculates the threshold index from an unnormalized histogram of data.
	 * Returns -1 if the threshold index cannot be found.
	 */
	long getThreshold(Histogram1d<?> histogram);

	/**
	 * Returns any message associated with the last call to getThreshold(). If
	 * getThreshold() last returned -1 the internal message may shed light on the
	 * issue. If getThreshold() is successful this message still may contain
	 * warning info.
	 */
	String getMessage();
}
