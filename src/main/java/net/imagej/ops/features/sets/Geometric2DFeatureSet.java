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

import net.imagej.ops.Ops;
import net.imagej.ops.featuresets.AbstractOpRefFeatureSet;
import net.imagej.ops.featuresets.DimensionBoundFeatureSet;
import net.imagej.ops.featuresets.FeatureSet;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} to calculate Geometric2DFeatureSet
 * 
 * @author Christian Dietz, University of Konstanz
 *
 * @param <I>
 * @param <O>
 */
@Plugin(type = FeatureSet.class, label = "Geometric Features 2D", description = "Calculates Geometric Features on 2D LabelRegions")
public class Geometric2DFeatureSet<L, O extends RealType<O>> extends AbstractOpRefFeatureSet<LabelRegion<L>, O>
		implements DimensionBoundFeatureSet<LabelRegion<L>, O> {
	
	private static final String PKG = "net.imagej.ops.Ops$Geometric$";

	@Parameter(required = false, label = "Size", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "Size") })
	private boolean isSizeActive = true;

	@Parameter(required = false, label = "Circularity", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "Circularity") })
	private boolean isCircularityActive = true;

	@Parameter(required = false, label = "Convexity", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "Convexity") })
	private boolean isConvexityActive = true;

	@Parameter(required = false, label = "Eccentricity", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "Eccentricity") })
	private boolean isEccentricityActive = true;

	@Parameter(required = false, label = "MainElongation", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "MainElongation") })
	private boolean isMainElongationActive = true;

	@Parameter(required = false, label = "FeretsAngle", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "FeretsAngle") })
	private boolean isFeretsAngleActive = true;

	@Parameter(required = false, label = "FeretsDiameter", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "FeretsDiameter") })
	private boolean isFeretsDiameterActive = true;

	@Parameter(required = false, label = "MajorAxis", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "MajorAxis") })
	private boolean isMajorAxisActive = true;

	@Parameter(required = false, label = "MinorAxis", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "MinorAxis") })
	private boolean isMinorAxisActive = true;

	@Parameter(required = false, label = "BoundarySize", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "BoundarySize") })
	private boolean isBoundarySizeActive = true;

	@Parameter(required = false, label = "Boxivity", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "Boxivity") })
	private boolean isBoxivityActive = true;

	@Parameter(required = false, label = "Roundness", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "Roundness") })
	private boolean isRoundnessActive = true;

	@Parameter(required = false, label = "Rugosity", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "Rugosity") })
	private boolean isRugosityActive = true;

	@Parameter(required = false, label = "Solidity", attrs = { @Attr(name = ATTR_FEATURE),
			@Attr(name = ATTR_PARAMS, value = ""),
			@Attr(name = ATTR_TYPE, value = PKG + "Solidity") })
	private boolean isSolidityActive = true;
	
	@Override
	public Map<NamedFeature, O> compute(final LabelRegion<L> input) {
		final Map<NamedFeature, O> res = new HashMap<NamedFeature, O>();

	@Override
	public int getMinDimensions() {
		return 2;
	}

	@Override
	public int getMaxDimensions() {
		return 2;
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == 2;
	}

}