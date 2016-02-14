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
