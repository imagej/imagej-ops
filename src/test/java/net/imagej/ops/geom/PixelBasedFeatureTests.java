/*-
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2017 Board of Regents of the University of
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
package net.imagej.ops.geom;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Geometric.BoundarySizeConvexHull;
import net.imagej.ops.features.AbstractFeatureTest;
import net.imagej.ops.geom.geom2d.DefaultBoundingBox;
import net.imagej.ops.geom.geom2d.DefaultBoxivityPolygon;
import net.imagej.ops.geom.geom2d.DefaultCircularity;
import net.imagej.ops.geom.geom2d.DefaultContour;
import net.imagej.ops.geom.geom2d.DefaultConvexHull2D;
import net.imagej.ops.geom.geom2d.DefaultConvexityPolygon;
import net.imagej.ops.geom.geom2d.DefaultEccentricity;
import net.imagej.ops.geom.geom2d.DefaultElongation;
import net.imagej.ops.geom.geom2d.DefaultFeretsDiameterForAngle;
import net.imagej.ops.geom.geom2d.DefaultMajorAxis;
import net.imagej.ops.geom.geom2d.DefaultMaximumFeretAngle;
import net.imagej.ops.geom.geom2d.DefaultMaximumFeretDiameter;
import net.imagej.ops.geom.geom2d.DefaultMinimumFeretAngle;
import net.imagej.ops.geom.geom2d.DefaultMinimumFeretDiameter;
import net.imagej.ops.geom.geom2d.DefaultMinorAxis;
import net.imagej.ops.geom.geom2d.DefaultPerimeterLength;
import net.imagej.ops.geom.geom2d.DefaultRoundness;
import net.imagej.ops.geom.geom2d.DefaultSizeConvexHullPolygon;
import net.imagej.ops.geom.geom2d.DefaultSizePolygon;
import net.imagej.ops.geom.geom2d.DefaultSmallestEnclosingRectangle;
import net.imagej.ops.geom.geom2d.DefaultSolidityPolygon;
import net.imagej.ops.geom.geom2d.DefaultVerticesCountConvexHullPolygon;
import net.imagej.ops.geom.geom2d.DefaultVerticesCountPolygon;
import net.imagej.ops.geom.geom2d.LabelRegionToPolygonConverter;
import net.imagej.ops.geom.pixelbased.DefaultBoundaryII;
import net.imagej.table.GenericTable;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.img.Img;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for polygon features.
 * 
 * A polygon is often extracted from a 2D label region.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
public class PixelBasedFeatureTests extends AbstractFeatureTest {

	private static final double EPSILON = 10e-12;
	private static IterableInterval<BitType> IMG;
	
	@Override
	@Before
	public void setup() {
		IMG = ops.threshold().apply(getTestImage2D(), new FloatType(1));
	}

	@Test
	public void boundarySize() {
		LabelRegion<String> roi = createLabelRegion(getTestImage2D(), 0, 255);
		assertEquals(357.4629867976521, ops.geom().boundarySize(roi).get(),
				EPSILON);
	}
	
	@Test
	public void size() {
		assertEquals(0, ops.geom().size(IMG).get(), EPSILON);
	}
}
