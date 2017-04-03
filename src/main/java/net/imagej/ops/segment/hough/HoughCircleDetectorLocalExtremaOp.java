
package net.imagej.ops.segment.hough;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.Localizable;
import net.imglib2.Point;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.Sampler;
import net.imglib2.algorithm.localextrema.LocalExtrema;
import net.imglib2.algorithm.localextrema.LocalExtrema.LocalNeighborhoodCheck;
import net.imglib2.algorithm.localextrema.RefinedPeak;
import net.imglib2.algorithm.localextrema.SubpixelLocalization;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;

import org.scijava.Cancelable;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.thread.ThreadService;

/**
 * A Hough circle detector based on finding local extrema in the vote image,
 * followed by non-maxima suppression.
 * <p>
 * This detector offers typically a faster processing than its DoG-based
 * counterpart, at the cost of accuracy and robustness.
 * <p>
 * It involves non-maxima suppression. The local extrema-based detector directly
 * looks for local maxima in the vote image. The vote image might be 'noisy':
 * several local maxima might correspond to the same true circle but be
 * separated by a couple of pixels. This will results in having two or more
 * circles detected very close to each other with a very similar radius.
 * Non-maxima suppression is an ad-hoc workaround for these artifacts. You order
 * all the circles by some quality factor (we use the sensitivity) and if two
 * circles are found too close (in my case, one inside one another) you just
 * keep the one with the best quality (lower sensitivity). This is not always
 * desirable if you know that some smaller circles are supposed to be inside
 * larger one. In that case you have to use the other detector.
 *
 * @author Jean-Yves Tinevez
 * @param <T> the type of the source vote image.
 */
@Plugin(type = Ops.Segment.DetectHoughCircleLE.class)
public class HoughCircleDetectorLocalExtremaOp<T extends RealType<T> & NativeType<T>>
	extends
	AbstractUnaryFunctionOp<RandomAccessibleInterval<T>, List<HoughCircle>>
	implements Cancelable, Ops.Segment.DetectHoughCircleLE
{

	@Parameter
	private ThreadService threadService;

	@Parameter(min = "1")
	private double minRadius;

	@Parameter(min = "1")
	private double stepRadius;

	/**
	 * Hough circle detection is a detection algorithm. Many of them returns a
	 * quality measure for what they detect (spot, circle, line,...) that reports
	 * how likely it is that it is not a spurious detection. The greater the
	 * quality, the more likely that it is a real one.
	 * <p>
	 * The sensitivity used here which varies as the inverse of this quality. The
	 * sensitivity of a circle appears as the minimal value the sensitivity
	 * settings must be set to incorporate it in the results.
	 */
	@Parameter(required = false, min = "0.1")
	private double sensitivity = 20.;

	@Override
	public List<HoughCircle> calculate(final RandomAccessibleInterval<T> input) {
		final int numDimensions = input.numDimensions();

		/*
		 * Find local extrema.
		 */

		final double threshold = 2. * Math.PI * minRadius / sensitivity;
		final T t = Util.getTypeFromInterval(input).createVariable();
		t.setReal(threshold);
		final LocalNeighborhoodCheck<Circle, T> maximumCheck =
			new LocalNeighborhoodCheck<Circle, T>()
			{

				@Override
				public <C extends Localizable & Sampler<T>> Circle check(final C center,
					final Neighborhood<T> neighborhood)
			{
					final T c = center.get();
					if (t.compareTo(c) > 0) return null;

					for (final T t1 : neighborhood)
						if (t1.compareTo(c) > 0) return null;

					final double val = c.getRealDouble();
					final double radius = minRadius + (center.getDoublePosition(
						numDimensions - 1)) * stepRadius;
					final double ls = 2. * Math.PI * radius / val;
					return new Circle(center, radius, ls);
				}
			};
		final ArrayList<Circle> peaks = LocalExtrema.findLocalExtrema(input,
			maximumCheck, threadService.getExecutorService());

		if (isCanceled()) return Collections.emptyList();

		/*
		 * Non-maxima suppression.
		 *
		 * Rule: when one circle has a center inside one another, we discard the
		 * one with the highest sensitivity.
		 */

		// Sort by ascending sensitivity.
		Collections.sort(peaks);

		final List<Circle> retained = new ArrayList<>();
		NEXT_CIRCLE:
		for (final Circle tested : peaks) {
			for (final Circle kept : retained) {
				if (kept.contains(tested)) continue NEXT_CIRCLE;
			}

			// Was not found in any circle, so we keep it.
			retained.add(tested);
		}

		if (isCanceled()) return Collections.emptyList();

		/*
		 * Refine local extrema.
		 */

		final SubpixelLocalization<Circle, T> spl = new SubpixelLocalization<>(
			numDimensions);
		spl.setAllowMaximaTolerance(true);
		spl.setMaxNumMoves(10);
		final ArrayList<RefinedPeak<Circle>> refined = spl.process(retained, input,
			input);

		if (isCanceled()) return Collections.emptyList();

		/*
		 * Create circles.
		 */

		final ArrayList<HoughCircle> circles = new ArrayList<>(refined.size());
		for (final RefinedPeak<Circle> peak : refined) {
			final double radius = minRadius + (peak.getDoublePosition(numDimensions -
				1)) * stepRadius;
			final double ls = 2. * Math.PI * radius / peak.getValue();
			if (ls < 0 || ls > sensitivity) continue;
			final RealPoint center = new RealPoint(numDimensions - 1);
			for (int d = 0; d < numDimensions - 1; d++)
				center.setPosition(peak.getDoublePosition(d), d);

			circles.add(new HoughCircle(center, radius, ls));
		}

		Collections.sort(circles);
		return circles;
	}

	// -- Cancelable methods --

	/** Reason for cancelation, or null if not canceled. */
	private String cancelReason;

	@Override
	public boolean isCanceled() {
		return cancelReason != null;
	}

	/** Cancels the command execution, with the given reason for doing so. */
	@Override
	public void cancel(final String reason) {
		cancelReason = reason == null ? "" : reason;
	}

	@Override
	public String getCancelReason() {
		return cancelReason;
	}

	/*
	 * INNER CLASSES
	 */

	/**
	 * A circle class defined on integer position with a sensitivity value.
	 *
	 * @author Jean-Yves Tinevez
	 */
	private final class Circle extends Point implements Comparable<Circle> {

		private final double radius;

		private final double sensitivity1;

		public Circle(final Localizable pos, final double radius,
			final double sensitivity)
		{
			super(pos);
			this.radius = radius;
			this.sensitivity1 = sensitivity;
		}

		@Override
		public int compareTo(final Circle o) {
			return sensitivity1 < o.sensitivity1 ? -1 : sensitivity1 > o.sensitivity1
				? +1 : 0;
		}

		public boolean contains(final RealLocalizable point) {
			final double dx = getDoublePosition(0) - point.getDoublePosition(0);
			final double dy = getDoublePosition(1) - point.getDoublePosition(1);
			final double dr2 = dx * dx + dy * dy;
			return dr2 <= radius * radius;
		}

	}
}
