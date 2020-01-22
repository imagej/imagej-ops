
package net.imagej.ops.coloc.saca;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import net.imagej.ops.coloc.IntComparator;

/**
 * Helper class for the AdaptiveKTau op.
 *
 * @author Shulei Wang
 * @author Curtis Rueden
 * @author Ellen T Arena
 */
final class WtKendallTau {

	private static final Comparator<double[]> SORT_X = (row1, row2) -> Double
		.compare(row1[0], row2[0]);

	private static final Comparator<double[]> SORT_Y = (row1, row2) -> Double
		.compare(row1[1], row2[1]);

	public static double calculate(final double[] X, final double[] Y,
		final double[] W, final double[][] combinedData, final int[] rankedindex,
		final double[] rankedw, final int[] index1, final int[] index2,
		final double[] w1, final double[] w2, final double[] cumw, final Random rng)
	{

		final double[][] rankedData = rank(X, Y, W, combinedData, rng);

		for (int i = 0; i < X.length; i++) {
			rankedindex[i] = (int) rankedData[i][0];
			rankedw[i] = rankedData[i][2];
		}

		final double swap = sort(rankedindex, rankedw, Integer::compare, index1,
			index2, w1, w2, cumw);
		final double tw = totw(W) / 2;

		final double tau = (tw - 2 * swap) / tw;

		return tau;
	}

	private static double totw(final double[] w) {
		double sumw = 0;
		double sumsquarew = 0;

		for (int i = 0; i < w.length; i++) {
			sumw += w[i];
			sumsquarew += w[i] * w[i];
		}

		final double result = sumw * sumw - sumsquarew;

		return result;
	}

	private static double[][] rank(final double[] IX, final double[] IY,
		final double[] IW, final double[][] combinedData, final Random rng)
	{

		for (int i = 0; i < IX.length; i++) {
			combinedData[i][0] = IX[i];
			combinedData[i][1] = IY[i];
			combinedData[i][2] = IW[i];
		}

		// sort X
		Arrays.sort(combinedData, SORT_X);

		rank1D(combinedData, 0, rng);
		
		// sort Y
		Arrays.sort(combinedData, SORT_Y);

		rank1D(combinedData, 1, rng);
		
		return combinedData;
	}

	private static void rank1D(final double[][] combinedData, final int dim, final Random rng) {
		int start = 0;
		int end = 1;
		int rank = 1;
		while (start < combinedData.length) {
			if(end < combinedData.length && combinedData[start][dim] == combinedData[end][dim]) {
				// tied value; count how many tied values there are in a row
				do {
					end++;
				}
				while(end < combinedData.length && combinedData[start][dim] == combinedData[end][dim]);
				// now assign unique rank randomly over these indices -- Fisher-Yates shuffle!
				for (int i = start; i < end - 1; i++) {
					final int newIndex = start + rng.nextInt(end - start);
					final double[] tmp = combinedData[i];
					combinedData[i] = combinedData[newIndex];
					combinedData[newIndex] = tmp;
				}
				rank += end - start;
			}
			for (int i = start; i < end; i++) {
				combinedData[i][dim] = rank++;
			}
			start = end;
			end++;
		}
	}

	private static double sort(final int[] data, final double[] weight,
		final IntComparator comparator, final int[] index1, final int[] index2,
		final double[] w1, final double[] w2, final double[] cumw)
	{
		double swap = 0;
		double tempswap;
		final int n = data.length;
		int step = 1;
		int begin;
		int begin2;
		int end;
		int k;

		for (int i = 0; i < n; i++) {
			index1[i] = data[i];
			w1[i] = weight[i];
		}

		while (step < n) {
			begin = 0;
			k = 0;
			cumw[0] = w1[0];
			for (int i = 1; i < n; i++) {
				cumw[i] = cumw[i - 1] + w1[i];
			}

			while (true) {
				begin2 = begin + step;
				end = begin2 + step;
				if (end > n) {
					if (begin2 > n) break;
					end = n;
				}
				int i = begin;
				int j = begin2;
				while (i < begin2 && j < end) {
					if (comparator.compare(index1[i], index1[j]) > 0) {
						if (i == 0) {
							tempswap = w1[j] * cumw[begin2 - 1];
						}
						else {
							tempswap = w1[j] * (cumw[begin2 - 1] - cumw[i - 1]);
						}
						swap = swap + tempswap;
						index2[k] = index1[j];
						w2[k++] = w1[j++];
					}
					else {
						index2[k] = index1[i];
						w2[k++] = w1[i++];
					}
				}
				if (i < begin2) {
					while (i < begin2) {
						index2[k] = index1[i];
						w2[k++] = w1[i++];
					}
				}
				else {
					while (j < end) {
						index2[k] = index1[j];
						w2[k++] = w1[j++];
					}
				}
				begin = end;
			}
			if (k < n) {
				while (k < n) {
					index2[k] = index1[k];
					w2[k] = w1[k];
					k++;
				}
			}
			for (int i = 0; i < n; i++) {
				index1[i] = index2[i];
				w1[i] = w2[i];
			}

			step *= 2;
		}
		return swap;
	}
}
