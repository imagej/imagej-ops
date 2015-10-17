///*
// * #%L
// * ImageJ software for multidimensional image processing and analysis.
// * %%
// * Copyright (C) 2014 - 2015 Board of Regents of the University of
// * Wisconsin-Madison, University of Konstanz and Brian Northan.
// * %%
// * Redistribution and use in source and binary forms, with or without
// * modification, are permitted provided that the following conditions are met:
// * 
// * 1. Redistributions of source code must retain the above copyright notice,
// *    this list of conditions and the following disclaimer.
// * 2. Redistributions in binary form must reproduce the above copyright notice,
// *    this list of conditions and the following disclaimer in the documentation
// *    and/or other materials provided with the distribution.
// * 
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
// * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// * POSSIBILITY OF SUCH DAMAGE.
// * #L%
// */
//package net.imagej.ops.features.sets;
//
//import org.scijava.plugin.Parameter;
//import org.scijava.plugin.Plugin;
//
//import net.imagej.ops.Ops.Geometric.BoundarySize;
//import net.imagej.ops.Ops.Geometric.Boxivity;
//import net.imagej.ops.Ops.Geometric.Circularity;
//import net.imagej.ops.Ops.Geometric.Convexity;
//import net.imagej.ops.Ops.Geometric.Eccentricity;
//import net.imagej.ops.Ops.Geometric.FeretsAngle;
//import net.imagej.ops.Ops.Geometric.FeretsDiameter;
//import net.imagej.ops.Ops.Geometric.MainElongation;
//import net.imagej.ops.Ops.Geometric.MajorAxis;
//import net.imagej.ops.Ops.Geometric.MinorAxis;
//import net.imagej.ops.Ops.Geometric.Roundness;
//import net.imagej.ops.Ops.Geometric.Rugosity;
//import net.imagej.ops.Ops.Geometric.Size;
//import net.imagej.ops.Ops.Geometric.Solidity;
//import net.imagej.ops.featuresets.AbstractOpRefFeatureSet;
//import net.imagej.ops.featuresets.DimensionBoundFeatureSet;
//import net.imagej.ops.featuresets.FeatureSet;
//import net.imglib2.roi.labeling.LabelRegion;
//import net.imglib2.type.numeric.RealType;
//
///**
// * {@link FeatureSet} to calculate Geometric2DFeatureSet
// * 
// * @author Christian Dietz, University of Konstanz
// *
// * @param <I>
// * @param <O>
// */
//@Plugin(type = FeatureSet.class, label = "Geometric Features 2D", description = "Calculates Geometric Features on 2D LabelRegions")
//public class Geometric2DFeatureSet<L, O extends RealType<O>> extends AbstractOpRefFeatureSet<LabelRegion<L>, O>
//		implements DimensionBoundFeatureSet<LabelRegion<L>, O> {
//
//	@Parameter(required = false, label = "Size")
//	private boolean isSizeActive = true;
//
//	@Parameter(required = false, label = "Circularity")
//	private boolean isCircularityActive = true;
//
//	@Parameter(required = false, label = "Convexity")
//	private boolean isConvexityActive = true;
//
//	@Parameter(required = false, label = "Eccentricity")
//	private boolean isEccentricityActive = true;
//
//	@Parameter(required = false, label = "MainElongation")
//	private boolean isMainElongationActive = true;
//
//	@Parameter(required = false, label = "FeretsAngle")
//	private boolean isFeretsAngleActive = true;
//
//	@Parameter(required = false, label = "FeretsDiameter")
//	private boolean isFeretsDiameterActive = true;
//
//	@Parameter(required = false, label = "MajorAxis")
//	private boolean isMajorAxisActive = true;
//
//	@Parameter(required = false, label = "MinorAxis")
//	private boolean isMinorAxisActive = true;
//
//	@Parameter(required = false, label = "BoundarySize")
//	private boolean isBoundarySizeActive = true;
//
//	@Parameter(required = false, label = "Boxivity")
//	private boolean isBoxivityActive = true;
//
//	@Parameter(required = false, label = "Roundness")
//	private boolean isRoundnessActive = true;
//
//	@Parameter(required = false, label = "Rugosity")
//	private boolean isRugosityActive = true;
//
//	@Parameter(required = false, label = "Solidity")
//	private boolean isSolidityActive = true;
//
//	public Geometric2DFeatureSet() {
//		// NB: Empty cofstruction
//	}
//
//	@Override
//	protected void initFeatures() {
//		setFeature(isSizeActive, Size.class);
//		setFeature(isCircularityActive, Circularity.class);
//		setFeature(isConvexityActive, Convexity.class);
//		setFeature(isEccentricityActive, Eccentricity.class);
//		setFeature(isMainElongationActive, MainElongation.class);
//		setFeature(isFeretsAngleActive, FeretsAngle.class);
//		setFeature(isFeretsDiameterActive, FeretsDiameter.class);
//		setFeature(isMajorAxisActive, MajorAxis.class);
//		setFeature(isMinorAxisActive, MinorAxis.class);
//		setFeature(isBoundarySizeActive, BoundarySize.class);
//		setFeature(isBoxivityActive, Boxivity.class);
//		setFeature(isRoundnessActive, Roundness.class);
//		setFeature(isRugosityActive, Rugosity.class);
//		setFeature(isSolidityActive, Solidity.class);
//	}
//
//	@Override
//	public int getMinDimensions() {
//		return 2;
//	}
//
//	@Override
//	public int getMaxDimensions() {
//		return 2;
//	}
//
//	@Override
//	public boolean conforms() {
//		return in().numDimensions() == 2;
//	}
//
//}