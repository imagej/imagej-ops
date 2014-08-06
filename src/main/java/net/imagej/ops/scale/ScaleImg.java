/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
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

package net.imagej.ops.scale;

import net.imagej.ops.Op;
import net.imagej.ops.normalize.Normalize;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.FlatIterationOrder;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.realtransform.RealViews;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.ItemIO;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Martin Horn
 */
@Plugin(type = Op.class, name = Scale.NAME, attrs = { @Attr(name = "aliases",
	value = Normalize.ALIASES) })
public class ScaleImg<T extends RealType<T>> implements Scale {

	@Parameter
	private Img<T> in;

	@Parameter
	/*Scale factors for each dimension*/
	private double[] scaleFactors;

	@Parameter
	private InterpolatorFactory<T, RandomAccessible<T>> interpolator;

	@Parameter(type = ItemIO.OUTPUT)
	private Img<T> out;

	@Override
	public void run() {
		if (in.numDimensions() != scaleFactors.length) {
			throw new IllegalArgumentException(
				"Less/more scale factors are provided than dimensions in the image.");
		}

		final long[] newDims = new long[in.numDimensions()];
		in.dimensions(newDims);
		for (int i = 0; i < Math.min(scaleFactors.length, in.numDimensions()); i++)
		{
			newDims[i] = Math.round(in.dimension(i) * scaleFactors[i]);
		}

		IntervalView<T> interval =
			Views.interval(Views.raster(RealViews.affineReal(Views.interpolate(Views
				.extendMirrorSingle(in), interpolator),
				new net.imglib2.realtransform.Scale(scaleFactors))), new FinalInterval(
				newDims));

		out = in.factory().create(newDims, in.firstElement().createVariable());
		Cursor<T> outC = out.cursor();
		if (in.iterationOrder().equals(new FlatIterationOrder(in))) {
			Cursor<T> viewC = Views.flatIterable(interval).cursor();
			while (outC.hasNext()) {
				outC.fwd();
				viewC.fwd();
				outC.get().set(viewC.get());
			}
		}
		else {
			RandomAccess<T> viewRA = interval.randomAccess();
			while (outC.hasNext()) {
				outC.fwd();
				viewRA.setPosition(outC);
				outC.get().set(viewRA.get());
			}

		}

	}
}
