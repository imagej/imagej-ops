/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2018 ImageJ developers.
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
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.outofbounds.OutOfBoundsMirrorFactory;
import net.imglib2.realtransform.RealViews;
import net.imglib2.realtransform.Scale;
import net.imglib2.util.Intervals;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

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
@Plugin(type = Ops.Transform.ScaleView.class, priority = Priority.HIGH+1)
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

	@Override
	public RandomAccessibleInterval<T> calculate(RandomAccessibleInterval<T> input) {
		final long[] newDims = Intervals.dimensionsAsLongArray(in());
		for (int i = 0; i < Math.min(scaleFactors.length, in().numDimensions()); i++) {
			newDims[i] = Math.round(in().dimension(i) * scaleFactors[i]);
		}

		IntervalView<T> interval = Views.interval(Views.raster(RealViews.affineReal(
			Views.interpolate(Views.extendMirrorSingle(input), interpolator),
			new Scale(scaleFactors))), new FinalInterval(newDims));

		return interval;
	}

	@Override
	public boolean conforms() {
		return in().numDimensions() == scaleFactors.length;
	}

}
