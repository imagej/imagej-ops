
package net.imagej.ops.segment.hough;

import net.imagej.ops.Ops;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessible;
import net.imglib2.img.Img;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Hough transform for binary images, with weights for each pixel.
 *
 * @author Jean-Yves Tinevez
 * @param <T> the type of source image. Must extend boolean type.
 */
@Plugin(type = Ops.Segment.TransformHoughCircle.class)
public class HoughTransformOpWeights<T extends BooleanType<T>, R extends RealType<R>>
	extends HoughTransformOpNoWeights<T>
{

	@Parameter(label = "Weights",
		description = "Weight image for the vote image.")
	private RandomAccessible<R> weights;

	@Override
	public void compute(final IterableInterval<T> input,
		final Img<DoubleType> output)
	{
		final long maxR = Math.max(minRadius, maxRadius);
		final long minR = Math.min(minRadius, maxRadius);
		maxRadius = maxR;
		minRadius = minR;
		final long nRadiuses = (maxRadius - minRadius) / stepRadius + 1;

		/*
		 * Hough transform.
		 */

		final DoubleType weight = new DoubleType(Double.NaN);
		final RandomAccess<R> raWeight = weights.randomAccess(input);
		final double sum = ops().stats().sum(input).getRealDouble();
		int progress = 0;

		final Cursor<T> cursor = input.localizingCursor();
		while (cursor.hasNext()) {
			cursor.fwd();
			if (!cursor.get().get()) continue;

			raWeight.setPosition(cursor);
			weight.set(raWeight.get().getRealDouble());

			for (int i = 0; i < nRadiuses; i++) {
				final IntervalView<DoubleType> slice = Views.hyperSlice(output, input
					.numDimensions(), i);
				final long r = minRadius + i * stepRadius;
				MidPointAlgorithm.add(Views.extendZero(slice), cursor, r, weight);
			}

			statusService.showProgress(++progress, (int) sum);
		}
	}
}
