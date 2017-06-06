package net.imagej.ops.coloc.icq;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractOp;
import net.imagej.ops.Ops;
import net.imglib2.type.numeric.RealType;
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
public class LiICQ<T extends RealType<T>, U extends RealType<U>> extends AbstractOp implements Ops.Coloc.ICQ {
	/** the resulting ICQ value. */
	@Parameter(type = ItemIO.OUTPUT)
	private double icqValue;

	@Parameter
	private Iterable<Pair<T, U>> samples;

	@Parameter
	private double mean1;

	@Parameter
	private double mean2;

	@Override
	public void run() {
		// variables to count the positive and negative results
		// of Li's product of the difference of means.
		long numPositiveProducts = 0;
		long numNegativeProducts = 0;
		// iterate over image
		for (final Pair<T, U> value : samples) {

			final double ch1 = value.getA().getRealDouble();
			final double ch2 = value.getB().getRealDouble();

			final double productOfDifferenceOfMeans = (mean1 - ch1) * (mean2 - ch2);

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
		icqValue = ((double) numPositiveProducts / (double) (numNegativeProducts + numPositiveProducts)) - 0.5;
	}
}
