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

import net.imagej.ops.features.AbstractFeatureSet;
import net.imagej.ops.features.FeatureSet;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.CircularityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.ConvexityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsAngleFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsDiameterFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.PerimeterFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RectangularityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RoundnessFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RugosityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.SolidityFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonAreaProvider;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonConvexHullAreaProvider;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonConvexHullPerimeterProvider;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonConvexHullProvider;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonFeretProvider;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonPerimeterProvider;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonSmallestEnclosingRectangleAreaProvider;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonSmallestEnclosingRectangleProvider;
import net.imagej.ops.geometric.polygon.Polygon;

import org.scijava.plugin.Plugin;

/**
 * {@link FeatureSet} containing Geometric Features.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 * 
 */
@Plugin(type = FeatureSet.class, label = "Geometric Features")
public class GeometricFeatureSet extends AbstractFeatureSet<Polygon> {

	@Override
	protected void init() {

		// add helper
		addInvisible(PolygonAreaProvider.class, getInput());
		addInvisible(PolygonPerimeterProvider.class, getInput());

		addInvisible(PolygonConvexHullProvider.class, getInput());
		addInvisible(PolygonConvexHullAreaProvider.class);
		addInvisible(PolygonConvexHullPerimeterProvider.class);

		addInvisible(PolygonSmallestEnclosingRectangleProvider.class,
				getInput());
		addInvisible(PolygonSmallestEnclosingRectangleAreaProvider.class);

		addInvisible(PolygonFeretProvider.class, getInput());

		// add features
		addVisible(AreaFeature.class);
		addVisible(PerimeterFeature.class);

		addVisible(CircularityFeature.class);
		addVisible(RectangularityFeature.class);
		addVisible(ConvexityFeature.class);
		addVisible(SolidityFeature.class);
		addVisible(RugosityFeature.class);
		addVisible(RoundnessFeature.class);

		// TODO: use moments to calculate ellipse (see EllipseFitter.java) from
		// ImageJ
		// addVisible(MajorAxisFeature.class);
		// addVisible(MinorAxisFeature.class);
		// addVisible(EccentricityFeature.class);
		// addVisible(ElongationFeature.class);
		// addVisible(DefRoundness.class);

		addVisible(FeretsDiameterFeature.class);
		addVisible(FeretsAngleFeature.class);
	}

}
