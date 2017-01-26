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

package net.imagej.ops.transform.scaleView;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Transform.ExtendView;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealRandomAccessible;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.realtransform.AffineRealRandomAccessible;
import net.imglib2.realtransform.RealViews;
import net.imglib2.realtransform.Scale;
import net.imglib2.util.Intervals;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.view.RandomAccessibleOnRealRandomAccessible;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Scales a {@link RandomAccessibleInterval} in each dimension with the provided
 * scale factors.
 * 
 * @author Martin Horn (University of Konstanz)
 * @author Stefan Helfrich (University of Konstanz)
 */
@Plugin(type = Ops.Transform.ScaleView.class, priority = Priority.HIGH_PRIORITY)
public class DefaultScaleView<T> extends
	AbstractUnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements Ops.Transform.ScaleView, Contingent
{

	/**
	 * Scale factors for each dimension of the input.
	 */
	@Parameter
	private double[] scaleFactors;

	@Parameter
	private InterpolatorFactory<T, RandomAccessible<T>> interpolator;

	@Parameter(required = false)
	private OutOfBoundsFactory<T, RandomAccessibleInterval<T>> outOfBoundsFactory =
		new OutOfBoundsMirrorFactory<>(OutOfBoundsMirrorFactory.Boundary.SINGLE);

	private UnaryFunctionOp<RandomAccessible<T>, RealRandomAccessible<T>> interpolateOp;
	private UnaryFunctionOp<RealRandomAccessible<T>, RandomAccessibleOnRealRandomAccessible<T>> rasterOp;
	private UnaryFunctionOp<RandomAccessible<T>, IntervalView<T>> intervalOp;
	private UnaryFunctionOp<RandomAccessibleInterval<T>, ExtendedRandomAccessibleInterval<T, RandomAccessibleInterval<T>>> extendOp;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize() {
		interpolateOp = (UnaryFunctionOp) Functions.unary(ops(),
			Ops.Transform.InterpolateView.class, RealRandomAccessible.class,
			RandomAccessible.class, interpolator);
		rasterOp = (UnaryFunctionOp) Functions.unary(ops(),
			Ops.Transform.RasterView.class,
			RandomAccessibleOnRealRandomAccessible.class, RealRandomAccessible.class);
		extendOp = (UnaryFunctionOp) Functions.unary(ops(), ExtendView.class, ExtendedRandomAccessibleInterval.class,
				RandomAccessibleInterval.class, outOfBoundsFactory);

		final long[] newDims = Intervals.dimensionsAsLongArray(in());
		for (int i = 0; i < Math.min(scaleFactors.length, in().numDimensions()); i++) {
			newDims[i] = Math.round(in().dimension(i) * scaleFactors[i]);
		}
		intervalOp = (UnaryFunctionOp) Functions.unary(ops(),
			Ops.Transform.IntervalView.class, IntervalView.class,
			RandomAccessible.class, new FinalInterval(newDims));
	}

	@Override
	public RandomAccessibleInterval<T> calculate(RandomAccessibleInterval<T> input) {
		final RealRandomAccessible<T> interpolated = interpolateOp
				.calculate(extendOp.calculate(input));
		final AffineRealRandomAccessible<T, AffineGet> transformed = RealViews
			.affineReal(interpolated, new Scale(scaleFactors));
		final RandomAccessibleOnRealRandomAccessible<T> rasterized = rasterOp
			.calculate(transformed);

		return intervalOp.calculate(rasterized);
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == scaleFactors.length;
	}

}
