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
package net.imagej.ops.geom;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.logic.BoolType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

/**
 * Namespace for Geometric
 * 
 * @author Daniel Seebacher, University of Konstanz
 */
@SuppressWarnings("unchecked")
@Plugin(type = Namespace.class)
public class Geometric2DNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "geometric2d";
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultContour.class)
	public Polygon contour(final RandomAccessibleInterval<BoolType> in,
		final boolean useJacobs, final boolean isInverted)
	{
		final Polygon result = (Polygon) ops().run(
			net.imagej.ops.geom.DefaultContour.class, in, useJacobs, isInverted);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultConvexHull.class)
	public Polygon convexhull(final Polygon in) {
		final Polygon result = (Polygon) ops().run(
			net.imagej.ops.geom.DefaultConvexHull.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultCenterOfGravity.class)
	public RealLocalizable centerofgravity(final Polygon in) {
		final RealLocalizable result = (RealLocalizable) ops().run(
			net.imagej.ops.geom.DefaultCenterOfGravity.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultBoundingBox.class)
	public Polygon boundingbox(final Polygon in) {
		final Polygon result = (Polygon) ops().run(
			net.imagej.ops.geom.DefaultBoundingBox.class, in);
		return result;
	}

	@OpMethod(
		op = net.imagej.ops.geom.DefaultSmallestEnclosingRectangle.class)
	public Polygon smallestenclosingrectangle(final Polygon in) {
		final Polygon result = (Polygon) ops().run(
			net.imagej.ops.geom.DefaultSmallestEnclosingRectangle.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultArea.class)
	public <O extends RealType<O>> O area(final Polygon in) {
		final O result = (O) ops().run(net.imagej.ops.geom.DefaultArea.class,
			in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultArea.class)
	public <O extends RealType<O>> O area(final O out, final Polygon in) {
		final O result = (O) ops().run(net.imagej.ops.geom.DefaultArea.class,
			out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultCircularity.class)
	public <O extends RealType<O>> O circularity(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultCircularity.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultCircularity.class)
	public <O extends RealType<O>> O circularity(final O out, final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultCircularity.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultConvexity.class)
	public <O extends RealType<O>> O convexity(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultConvexity.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultConvexity.class)
	public <O extends RealType<O>> O convexity(final O out, final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultConvexity.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultEccentricity.class)
	public <O extends RealType<O>> O eccentricity(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultEccentricity.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultEccentricity.class)
	public <O extends RealType<O>> O eccentricity(final O out, final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultEccentricity.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultElongation.class)
	public <O extends RealType<O>> O elongation(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultElongation.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultElongation.class)
	public <O extends RealType<O>> O elongation(final O out, final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultElongation.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultFeret.class)
	public Pair<RealLocalizable, RealLocalizable> feret(final Polygon in) {
		final Pair<RealLocalizable, RealLocalizable> result =
			(Pair<RealLocalizable, RealLocalizable>) ops().run(
				net.imagej.ops.geom.DefaultFeret.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultFeretsAngle.class)
	public <O extends RealType<O>> O feretsAngle(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultFeretsAngle.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultFeretsAngle.class)
	public <O extends RealType<O>> O feretsAngle(final O out, final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultFeretsAngle.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultFeretsDiameter.class)
	public <O extends RealType<O>> O feretsDiameter(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultFeretsDiameter.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultFeretsDiameter.class)
	public <O extends RealType<O>> O feretsDiameter(final O out,
		final Polygon in)
	{
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultFeretsDiameter.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultMinorMajorAxis.class)
	public Pair<DoubleType, DoubleType> minorMajorAxis(final Polygon in) {
		final Pair<DoubleType, DoubleType> result =
			(Pair<DoubleType, DoubleType>) ops().run(
				net.imagej.ops.geom.DefaultMinorMajorAxis.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultMajorAxis.class)
	public <O extends RealType<O>> O majorAxis(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultMajorAxis.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultMajorAxis.class)
	public <O extends RealType<O>> O majorAxis(final O out, final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultMajorAxis.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultMinorAxis.class)
	public <O extends RealType<O>> O minorAxis(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultMinorAxis.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultMinorAxis.class)
	public <O extends RealType<O>> O minorAxis(final O out, final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultMinorAxis.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultPerimeter.class)
	public <O extends RealType<O>> O perimeter(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultPerimeter.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultPerimeter.class)
	public <O extends RealType<O>> O perimeter(final O out, final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultPerimeter.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultRectangularity.class)
	public <O extends RealType<O>> O rectangularity(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultRectangularity.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultRectangularity.class)
	public <O extends RealType<O>> O rectangularity(final O out,
		final Polygon in)
	{
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultRectangularity.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultRoundness.class)
	public <O extends RealType<O>> O roundness(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultRoundness.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultRoundness.class)
	public <O extends RealType<O>> O roundness(final O out, final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultRoundness.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultRugosity.class)
	public <O extends RealType<O>> O rugosity(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultRugosity.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultRugosity.class)
	public <O extends RealType<O>> O rugosity(final O out, final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultRugosity.class, out, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultSolidity.class)
	public <O extends RealType<O>> O solidity(final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultSolidity.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geom.DefaultSolidity.class)
	public <O extends RealType<O>> O solidity(final O out, final Polygon in) {
		final O result = (O) ops().run(
			net.imagej.ops.geom.DefaultSolidity.class, out, in);
		return result;
	}
}
