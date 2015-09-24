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
package net.imagej.ops.geometric3d;

import java.util.HashSet;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imagej.ops.descriptor3d.DefaultFacets;
import net.imagej.ops.descriptor3d.MarchingCubes;
import net.imagej.ops.descriptor3d.Vertex;
import net.imglib2.roi.IterableRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Namespace for geometric 3D features.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz
 *
 */
@Plugin(type = Namespace.class)
public class Geometric3DNamespace extends AbstractNamespace {

	@Override
	public String getName() {
		return "geometric3d";
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultCompactnessFeature.class)
	public <B extends BooleanType<B>> DoubleType Compactness(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geometric3d.DefaultCompactnessFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultConvexHullSurfaceAreaFeature.class)
	public <B extends BooleanType<B>> DoubleType ConvexHullSurfaceArea(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops()
				.run(net.imagej.ops.geometric3d.DefaultConvexHullSurfaceAreaFeature.class,
						in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultConvexHullSurfacePixelFeature.class)
	public <B extends BooleanType<B>> DoubleType ConvexHullSurfacePixel(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops()
				.run(net.imagej.ops.geometric3d.DefaultConvexHullSurfacePixelFeature.class,
						in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultConvexHullVolumeFeature.class)
	public <B extends BooleanType<B>> DoubleType ConvexHullVolume(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops()
				.run(net.imagej.ops.geometric3d.DefaultConvexHullVolumeFeature.class,
						in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultConvexityFeature.class)
	public <B extends BooleanType<B>> DoubleType Convexity(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geometric3d.DefaultConvexityFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultMainElongationFeature.class)
	public <B extends BooleanType<B>> DoubleType MainElongation(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geometric3d.DefaultMainElongationFeature.class,
				in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultMedianElongationFeature.class)
	public <B extends BooleanType<B>> DoubleType MedianElongation(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops()
				.run(net.imagej.ops.geometric3d.DefaultMedianElongationFeature.class,
						in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultRugosityFeature.class)
	public <B extends BooleanType<B>> DoubleType Rugosity(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geometric3d.DefaultRugosityFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultSolidityFeature.class)
	public <B extends BooleanType<B>> DoubleType Solidity(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geometric3d.DefaultSolidityFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultSparenessFeature.class)
	public <B extends BooleanType<B>> DoubleType Spareness(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geometric3d.DefaultSparenessFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultSphericityFeature.class)
	public <B extends BooleanType<B>> DoubleType Sphericity(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geometric3d.DefaultSphericityFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultSurfaceAreaFeature.class)
	public <B extends BooleanType<B>> DoubleType SurfaceArea(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geometric3d.DefaultSurfaceAreaFeature.class, in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultSurfacePixelFeature.class)
	public <B extends BooleanType<B>> DoubleType SurfacePixel(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops()
				.run(net.imagej.ops.geometric3d.DefaultSurfacePixelFeature.class,
						in);
		return result;
	}

	@OpMethod(op = net.imagej.ops.geometric3d.DefaultVolumeFeature.class)
	public <B extends BooleanType<B>> DoubleType Volume(
			final IterableRegion<B> in) {
		final DoubleType result = (DoubleType) ops().run(
				net.imagej.ops.geometric3d.DefaultVolumeFeature.class, in);
		return result;
	}
}
