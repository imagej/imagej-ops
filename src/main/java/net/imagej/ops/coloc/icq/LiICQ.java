package net.imagej.ops.coloc.icq;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.IterablePair;
import net.imglib2.util.Pair;

/**
 * This algorithm calculates Li et al.'s ICQ (intensity correlation quotient).
 *
 * @param <T>
 *            Type of the first image
 * @param <U>
 *            Type of the second image
 */
@Plugin(type = Ops.Coloc.ICQ.class)
public class LiICQ<T extends RealType<T>, U extends RealType<U>>
		extends AbstractBinaryFunctionOp<Iterable<T>, Iterable<U>, Double> implements Ops.Coloc.ICQ {

	@Parameter(required = false)
	private DoubleType mean1;

	@Parameter(required = false)
	private DoubleType mean2;

	@Override
	public Double calculate(final Iterable<T> image1, final Iterable<U> image2) {

		final Iterable<Pair<T, U>> samples = new IterablePair<>(image1, image2);

		final double m1 = mean1 == null ? computeMeanOf(image1) : mean1.get();
		final double m2 = mean2 == null ? computeMeanOf(image2) : mean2.get();

		// variables to count the positive and negative results
		// of Li's product of the difference of means.
		long numPositiveProducts = 0;
		long numNegativeProducts = 0;
		// iterate over image
		for (final Pair<T, U> value : samples) {

			final double ch1 = value.getA().getRealDouble();
			final double ch2 = value.getB().getRealDouble();

			final double productOfDifferenceOfMeans = (m1 - ch1) * (m2 - ch2);

			// check for positive and negative values
			if (productOfDifferenceOfMeans < 0.0)
				++numNegativeProducts;
			else
				++numPositiveProducts;
		}

		/*
		 * calculate Li's ICQ value by dividing the amount of "positive pixels"
		 * to the total number of pixels. Then shift it in the -0.5,0.5 range.
		 */
		final double icqValue = ((double) numPositiveProducts / (double) (numNegativeProducts + numPositiveProducts))
				- 0.5;
		return icqValue;
	}

	private <V extends RealType<V>> double computeMeanOf(final Iterable<V> in) {
		return ops().stats().mean(in).getRealDouble();
	}
}
