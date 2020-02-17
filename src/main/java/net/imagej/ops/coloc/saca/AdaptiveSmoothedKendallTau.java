
package net.imagej.ops.coloc.saca;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import net.imglib2.Cursor;
import net.imglib2.Localizable;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.loops.LoopBuilder;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Intervals;
import net.imglib2.util.Localizables;
import net.imglib2.util.Util;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;
import net.imglib2.view.composite.CompositeIntervalView;
import net.imglib2.view.composite.GenericComposite;

/**
 * Adapted from Shulei's original Java code for AdaptiveSmoothedKendallTau from
 * his RKColocal R package.
 * (https://github.com/lakerwsl/RKColocal/blob/master/RKColocal_0.0.1.0000.tar.gz)
 *
 * @author Shulei Wang
 * @author Curtis Rueden
 * @author Ellen T Arena
 */
public final class AdaptiveSmoothedKendallTau {

	private AdaptiveSmoothedKendallTau() {}

	public static <I extends RealType<I>, O extends RealType<O>> void execute(
		final RandomAccessibleInterval<I> image1,
		final RandomAccessibleInterval<I> image2, final I thres1, final I thres2,
		final RandomAccessibleInterval<O> result, final long seed)
	{
		execute(image1, image2, thres1, thres2, new DoubleType(), result, seed);
	}

	public static <I extends RealType<I>, T extends RealType<T>, O extends RealType<O>> void execute(
		final RandomAccessibleInterval<I> image1,
		final RandomAccessibleInterval<I> image2, final I thres1, final I thres2,
		final T intermediate, final RandomAccessibleInterval<O> result,
		final long seed)
	{
		final Function<RandomAccessibleInterval<I>, RandomAccessibleInterval<T>> factory =
			img -> Util.getSuitableImgFactory(img, intermediate).create(img);
		execute(image1, image2, thres1, thres2, factory, result, seed);
	}

	public static <I extends RealType<I>, T extends RealType<T>, O extends RealType<O>> void execute(
		final RandomAccessibleInterval<I> image1,
		final RandomAccessibleInterval<I> image2, final I thres1, final I thres2,
		Function<RandomAccessibleInterval<I>, RandomAccessibleInterval<T>> factory,
		final RandomAccessibleInterval<O> result, final long seed)
	{
		final long nr = image1.dimension(1);
		final long nc = image1.dimension(0);
		final RandomAccessibleInterval<T> oldtau = factory.apply(image1);
		final RandomAccessibleInterval<T> newtau = factory.apply(image1);
		final RandomAccessibleInterval<T> oldsqrtN = factory.apply(image1);
		final RandomAccessibleInterval<T> newsqrtN = factory.apply(image1);
		final List<RandomAccessibleInterval<T>> stop = new ArrayList<>();
		for (int s = 0; s < 3; s++)
			stop.add(factory.apply(image1));
		final double Dn = Math.sqrt(Math.log(nr * nc)) * 2;
		final int TU = 15;
		final int TL = 8;
		final double Lambda = Dn;

		LoopBuilder.setImages(oldsqrtN).forEachPixel(t -> t.setOne());

		double size = 1;
		final double stepsize = 1.15; // empirically the best, but could have users
																	// set
		int intSize;
		boolean IsCheck = false;

		final Random rng = new Random(seed);

		for (int s = 0; s < TU; s++) {
			intSize = (int) Math.floor(size);
			singleiteration(image1, image2, thres1, thres2, stop, oldtau, oldsqrtN,
				newtau, newsqrtN, result, Lambda, Dn, intSize, IsCheck, rng);
			size *= stepsize;
			if (s == TL) {
				IsCheck = true;
				LoopBuilder.setImages(stop.get(1), stop.get(2), newtau, newsqrtN)
					.forEachPixel((ts1, ts2, tTau, tSqrtN) -> {
						ts1.set(tTau);
						ts2.set(tSqrtN);
					});
			}
		}
	}

	private static <I extends RealType<I>, T extends RealType<T>, O extends RealType<O>>
		void singleiteration(final RandomAccessibleInterval<I> image1,
			final RandomAccessibleInterval<I> image2, final I thres1, final I thres2,
			final List<RandomAccessibleInterval<T>> stop,
			final RandomAccessibleInterval<T> oldtau,
			final RandomAccessibleInterval<T> oldsqrtN,
			final RandomAccessibleInterval<T> newtau,
			final RandomAccessibleInterval<T> newsqrtN,
			final RandomAccessibleInterval<O> result, final double Lambda,
			final double Dn, final int Bsize, final boolean isCheck, final Random rng)
	{
		final double[][] kernel = kernelGenerate(Bsize);

		final long[] rowrange = new long[4];
		final long[] colrange = new long[4];
		final int totnum = (2 * Bsize + 1) * (2 * Bsize + 1);
		final double[] LocX = new double[totnum];
		final double[] LocY = new double[totnum];
		final double[] LocW = new double[totnum];
		final double[][] combinedData = new double[totnum][3];
		final int[] rankedindex = new int[totnum];
		final double[] rankedw = new double[totnum];
		final int[] index1 = new int[totnum];
		final int[] index2 = new int[totnum];
		final double[] w1 = new double[totnum];
		final double[] w2 = new double[totnum];
		final double[] cumw = new double[totnum];

		RandomAccessibleInterval<T> workingImageStack = Views.stack(oldtau, newtau, oldsqrtN, newsqrtN, stop.get(0), stop.get(1), stop.get(2));
		CompositeIntervalView<T, ? extends GenericComposite<T>> workingImage =
			Views.collapse(workingImageStack);

		IntervalView<Localizable> positions = Views.interval( Localizables.randomAccessible(result.numDimensions() ), result );
		final long nr = result.dimension(1);
		final long nc = result.dimension(0);
		final RandomAccess<I> gdImage1 = image1.randomAccess();
		final RandomAccess<I> gdImage2 = image2.randomAccess();
		final RandomAccess<T> gdTau = oldtau.randomAccess();
		final RandomAccess<T> gdSqrtN = oldsqrtN.randomAccess();
		LoopBuilder.setImages(positions, result, workingImage).forEachPixel((pos, resPixel, workingPixel) -> {
			T oldtauPix = workingPixel.get(0);
			T newtauPix = workingPixel.get(1);
			T oldsqrtNPix = workingPixel.get(2);
			T newsqrtNPix = workingPixel.get(3);
			T stop0Pix = workingPixel.get(4);
			T stop1Pix = workingPixel.get(5);
			T stop2Pix = workingPixel.get(6);
			final long row = pos.getLongPosition(1);
			updateRange(row, Bsize, nr, rowrange);
			if (isCheck) {
				if (stop0Pix.getRealDouble() != 0) {
					return;
				}
			}
			final long col = pos.getLongPosition(0);
			updateRange(col, Bsize, nc, colrange);
			getData(Dn, kernel, gdImage1, gdImage2, gdTau, gdSqrtN, LocX, LocY, LocW,
				rowrange, colrange, totnum);
			newsqrtNPix.setReal(Math.sqrt(NTau(thres1, thres2, LocW, LocX,
				LocY)));
			if (newsqrtNPix.getRealDouble() <= 0) {
				newtauPix.setZero();
				resPixel.setZero();
			}
			else {
				final double tau = WtKendallTau.calculate(LocX, LocY, LocW, combinedData,
					rankedindex, rankedw, index1, index2, w1, w2, cumw, rng);
				newtauPix.setReal(tau);
				resPixel.setReal(tau * newsqrtNPix.getRealDouble() * 1.5);
			}

			if (isCheck) {
				final double taudiff = Math.abs(stop1Pix.getRealDouble() - newtauPix
					.getRealDouble()) * stop2Pix.getRealDouble();
				if (taudiff > Lambda) {
					stop0Pix.setOne();
					newtauPix.set(oldtauPix);
					newsqrtNPix.set(oldsqrtNPix);
				}
			}
		});		
		
		// TODO: instead of copying pixels here, swap oldTau and newTau every time.
		// :-)
		LoopBuilder.setImages(oldtau, newtau, oldsqrtN, newsqrtN).forEachPixel((
			tOldTau, tNewTau, tOldSqrtN, tNewSqrtN) -> {
			tOldTau.set(tNewTau);
			tOldSqrtN.set(tNewSqrtN);
		});

	}

	private static <I extends RealType<I>, T extends RealType<T>> void getData(
		final double Dn, final double[][] w, final RandomAccess<I> i1RA,
		final RandomAccess<I> i2RA, final RandomAccess<T> tau,
		final RandomAccess<T> sqrtN, final double[] sx, final double[] sy,
		final double[] sw, final long[] rowrange, final long[] colrange,
		final int totnum)
	{
		// TODO: Decide if this cast is OK.
		int kernelk = (int) (rowrange[0] - rowrange[2] + rowrange[3]);
		int kernell;
		int index = 0;
		double taudiffabs;

		sqrtN.setPosition(colrange[2], 0);
		sqrtN.setPosition(rowrange[2], 1);
		final double sqrtNValue = sqrtN.get().getRealDouble();

		for (long k = rowrange[0]; k <= rowrange[1]; k++) {
			i1RA.setPosition(k, 1);
			i2RA.setPosition(k, 1);
			sqrtN.setPosition(k, 1);
			// TODO: Double check cast.
			kernell = (int) (colrange[0] - colrange[2] + colrange[3]);
			for (long l = colrange[0]; l <= colrange[1]; l++) {
				i1RA.setPosition(l, 0);
				i2RA.setPosition(l, 0);
				sqrtN.setPosition(l, 0);
				sx[index] = i1RA.get().getRealDouble();
				sy[index] = i2RA.get().getRealDouble();
				sw[index] = w[kernelk][kernell];

				tau.setPosition(l, 0);
				tau.setPosition(k, 1);
				final double tau1 = tau.get().getRealDouble();

				tau.setPosition(colrange[2], 0);
				tau.setPosition(rowrange[2], 1);
				final double tau2 = tau.get().getRealDouble();

				taudiffabs = Math.abs(tau1 - tau2) * sqrtNValue;
				taudiffabs = taudiffabs / Dn;
				if (taudiffabs < 1) sw[index] = sw[index] * (1 - taudiffabs) * (1 -
					taudiffabs);
				else sw[index] = sw[index] * 0;
				kernell++;
				index++;
			}
			kernelk++;
		}
		while (index < totnum) {
			sx[index] = 0;
			sy[index] = 0;
			sw[index] = 0;
			index++;
		}
	}

	private static void updateRange(final long location, final int radius,
		final long boundary, final long[] range)
	{
		range[0] = location - radius;
		if (range[0] < 0) range[0] = 0;
		range[1] = location + radius;
		if (range[1] >= boundary) range[1] = boundary - 1;
		range[2] = location;
		range[3] = radius;
	}

	private static <I extends RealType<I>> double NTau(final I thres1,
		final I thres2, final double[] w, final double[] x, final double[] y)
	{
		double sumW = 0;
		double sumsqrtW = 0;
		double tempW;

		for (int index = 0; index < w.length; index++) {
			if (x[index] < thres1.getRealDouble() || y[index] < thres2
				.getRealDouble()) w[index] = 0;
			tempW = w[index];
			sumW += tempW;
			tempW = tempW * w[index];
			sumsqrtW += tempW;
		}
		double NW;
		final double Denomi = sumW * sumW;
		if (Denomi <= 0) {
			NW = 0;
		}
		else {
			NW = Denomi / sumsqrtW;
		}
		return NW;
	}

	private static double[][] kernelGenerate(final int size) {
		final int L = size * 2 + 1;
		final double[][] kernel = new double[L][L];
		final int center = size;
		double temp;
		final double Rsize = size * Math.sqrt(2.5);

		for (int i = 0; i <= size; i++) {
			for (int j = 0; j <= size; j++) {
				temp = Math.sqrt(i * i + j * j) / Rsize;
				if (temp >= 1) temp = 0;
				else temp = 1 - temp;
				kernel[center + i][center + j] = temp;
				kernel[center - i][center + j] = temp;
				kernel[center + i][center - j] = temp;
				kernel[center - i][center - j] = temp;
			}
		}
		return kernel;
	}
}
