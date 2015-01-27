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

package net.imagej.ops.features.sets;

import net.imagej.ops.OpRef;
import net.imagej.ops.features.AutoResolvingFeatureSet;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.CircularityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.ConvexityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.EccentricityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.ElongationFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsAngleFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsDiameterFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MajorAxisFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MinorAxisFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.PerimeterFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RectangularityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RoundnessFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RugosityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.SolidityFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.MinorMajorAxisOp;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonAreaOp;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonConvexHullAreaOp;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonConvexHullOp;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonConvexHullPerimeterOp;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonFeretOp;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonPerimeterOp;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonSmallestEnclosingRectangleAreaOp;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonSmallestEnclosingRectangleOp;
import net.imagej.ops.functionbuilder.OutputOpRef;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} containing Geometric Features.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * 
 */
@Plugin(type = FeatureSet.class, label = "Geometric Features")
public class GeometricFeatureSet extends
		AutoResolvingFeatureSet<Polygon, DoubleType> {

	public GeometricFeatureSet() {
		super(new DoubleType());

		// add helper
		addHiddenOp(new OpRef(PolygonAreaOp.class));
		addHiddenOp(new OpRef(PolygonPerimeterOp.class));
		addHiddenOp(new OpRef(PolygonConvexHullOp.class));
		addHiddenOp(new OpRef(PolygonConvexHullAreaOp.class));
		addHiddenOp(new OpRef(PolygonConvexHullPerimeterOp.class));
		addHiddenOp(new OpRef(PolygonSmallestEnclosingRectangleOp.class));
		addHiddenOp(new OpRef(PolygonSmallestEnclosingRectangleAreaOp.class));
		addHiddenOp(new OpRef(PolygonFeretOp.class));
		addHiddenOp(new OpRef(MinorMajorAxisOp.class));

		// add features
		addOutputOp(new OutputOpRef<DoubleType>(AreaFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(PerimeterFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(CircularityFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(RectangularityFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(ConvexityFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(SolidityFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(RugosityFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(ElongationFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(MajorAxisFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(MinorAxisFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(EccentricityFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(RoundnessFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(FeretsDiameterFeature.class,
				DoubleType.class));
		addOutputOp(new OutputOpRef<DoubleType>(FeretsAngleFeature.class,
				DoubleType.class));
	}
}
