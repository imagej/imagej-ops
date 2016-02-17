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

package net.imagej.ops.features.sets;

import net.imagej.ops.featuresets.AbstractOpRefFeatureSet;
import net.imagej.ops.featuresets.FeatureSet;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} to calculate first order statistic features
 * 
 * @author Christian Dietz, University of Konstanz
 * @author jaywarrick
 *
 * @param <I>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "Image Moments Features", description = "Calculates the Image Moments Features")
public class ImageMomentsFeatureSet<T, O extends RealType<O>> extends AbstractOpRefFeatureSet<IterableInterval<T>, O> {

	private static final String PKG = "net.imagej.ops.Ops$ImageMoments$";
	
	@Parameter(required = false, label = "CentralMoment00", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "CentralMoment00") })
	private boolean isCentralMoment00Active = true;

	@Parameter(required = false, label = "CentralMoment01", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "CentralMoment01") })
	private boolean isCentralMoment01Active = true;
	
	@Parameter(required = false, label = "CentralMoment02", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "CentralMoment02") })
	private boolean isCentralMoment02Active = true;
	
	@Parameter(required = false, label = "CentralMoment03", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "CentralMoment03") })
	private boolean isCentralMoment03Active = true;
	
	@Parameter(required = false, label = "CentralMoment10", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "CentralMoment10") })
	private boolean isCentralMoment10Active = true;
	
	@Parameter(required = false, label = "CentralMoment11", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "CentralMoment11") })
	private boolean isCentralMoment11Active = true;
	
	@Parameter(required = false, label = "CentralMoment12", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "CentralMoment12") })
	private boolean isCentralMoment12Active = true;
	
	@Parameter(required = false, label = "CentralMoment20", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "CentralMoment20") })
	private boolean isCentralMoment20Active = true;
	
	@Parameter(required = false, label = "CentralMoment21", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "CentralMoment21") })
	private boolean isCentralMoment21Active = true;
	
	@Parameter(required = false, label = "CentralMoment30", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "CentralMoment30") })
	private boolean isCentralMoment30Active = true;
	
	@Parameter(required = false, label = "HuMoment1", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "HuMoment1") })
	private boolean isHuMoment1Active = true;
	
	@Parameter(required = false, label = "HuMoment2", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "HuMoment2") })
	private boolean isHuMoment2Active = true;
	
	@Parameter(required = false, label = "HuMoment3", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "HuMoment3") })
	private boolean isHuMoment3Active = true;
	
	@Parameter(required = false, label = "HuMoment4", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "HuMoment4") })
	private boolean isHuMoment4Active = true;
	
	@Parameter(required = false, label = "HuMoment5", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "HuMoment5") })
	private boolean isHuMoment5Active = true;
	
	@Parameter(required = false, label = "HuMoment6", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "HuMoment6") })
	private boolean isHuMoment6Active = true;
	
	@Parameter(required = false, label = "HuMoment7", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "HuMoment7") })
	private boolean isHuMoment7Active = true;
	
	@Parameter(required = false, label = "Moment00", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Moment00") })
	private boolean isMoment00Active = true;
	
	@Parameter(required = false, label = "Moment01", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Moment01") })
	private boolean isMoment01Active = true;
	
	@Parameter(required = false, label = "Moment10", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Moment10") })
	private boolean isMoment10Active = true;
	
	@Parameter(required = false, label = "Moment11", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_TYPE, value = PKG + "Moment11") })
	private boolean isMoment11Active = true;

	public ImageMomentsFeatureSet() {
		// NB: Empty cofstruction
	}

}