/*
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

package net.imagej.ops.segment;

import java.util.List;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.roi.geom.real.Sphere;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * The segment namespace contains segmentation operations.
 * 
 * @author Gabe Selzer
 */
@Plugin(type = Namespace.class)
public class SegmentNamespace extends AbstractNamespace {

	// -- Segmentation namespace ops --

	/**
	 * Performs the Hough Circle Transform on a set radius
	 * 
	 * @param in
	 * @param out
	 * @param radius
	 * @param threshold
	 * @return
	 */
	@OpMethod(op = net.imagej.ops.segment.hough.CircleTransform.class)
	public <T extends RealType<T>> List<Sphere> circleTransform(
		final RandomAccessibleInterval<T> in, final List<Sphere> out, int radius,
		int threshold)
	{
		@SuppressWarnings("unchecked")
		final List<Sphere> result = (List<Sphere>) ops().run(
			net.imagej.ops.Ops.Segment.HoughCircle.class, in, out, radius, threshold);
		return result;
	}

	/**
	 * Performs the Hough Circle Transform on a variable radius.
	 * 
	 * @param in
	 * @param out
	 * @param radius
	 * @param threshold
	 * @return
	 */
	@OpMethod(
		op = net.imagej.ops.segment.hough.CircleTransformVariableRadius.class)
	public <T extends RealType<T>> List<Sphere> circleTransform(
		final RandomAccessibleInterval<T> in, final List<Sphere> out, int minRadius,
		int maxRadius, int stepRadius, int threshold)
	{
		@SuppressWarnings("unchecked")
		final List<Sphere> result = (List<Sphere>) ops().run(
			net.imagej.ops.Ops.Segment.HoughCircle.class, in, out, minRadius,
			maxRadius, stepRadius, threshold);
		return result;
	}

	@Override
	public String getName() {
		return "segment";
	}

}
