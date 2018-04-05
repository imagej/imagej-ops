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
package net.imagej.ops.create.img;

import net.imglib2.Dimensions;
import net.imglib2.Interval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.ImgView;
import net.imglib2.type.Type;
import net.imglib2.view.Views;

/**
 * Utility methods for working with {@link Img} objects.
 * 
 * @author Curtis Rueden
 */
public final class Imgs {

	private Imgs() {
		// NB: Prevent instantiation of utility class.
	}

	/**
	 * Creates an {@link Img}.
	 *
	 * @param factory The {@link ImgFactory} to use to create the image.
	 * @param dims The dimensional bounds of the newly created image. If the given
	 *          bounds are an {@link Interval}, the resultant {@link Img} will
	 *          have the same min/max as that {@link Interval} as well (see
	 *          {@link #adjustMinMax} for details).
	 * @param type The component type of the newly created image.
	 * @return the newly created {@link Img}
	 */
	public static <T extends Type<T>> Img<T> create(final ImgFactory<T> factory,
		final Dimensions dims, final T type)
	{
		return Imgs.adjustMinMax(factory.create(dims, type), dims);
	}

	/**
	 * Adjusts the given {@link Img} to match the bounds of the specified
	 * {@link Interval}.
	 * 
	 * @param img An image whose min/max bounds might need adjustment.
	 * @param minMax An {@link Interval} whose min/max bounds to use when
	 *          adjusting the image. If the provided {@code minMax} object is not
	 *          an {@link Interval}, no adjustment is performed.
	 * @return A wrapped version of the input {@link Img} with bounds adjusted to
	 *         match the provided {@link Interval}, if any; or the input image
	 *         itself if no adjustment was needed/possible.
	 */
	public static <T extends Type<T>> Img<T> adjustMinMax(final Img<T> img,
		final Object minMax)
	{
		if (!(minMax instanceof Interval)) return img;
		final Interval interval = (Interval) minMax;

		final long[] min = new long[interval.numDimensions()];
		interval.min(min);
		for (int d = 0; d < min.length; d++) {
			if (min[d] != 0) {
				return ImgView.wrap(Views.translate(img, min), img.factory());
			}
		}
		return img;
	}

}
