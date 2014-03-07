
package imagej.ops.slicer;

import net.imglib2.FinalInterval;
import net.imglib2.Interval;

public class HyperSliceUtils {

	private HyperSliceUtils() {
		// utility class
	}

	public static Interval[] resolveIntervals(final int[] selected,
		final Interval in)
	{

		final int totalSteps = getNumIterationSteps(selected, in);
		final Interval[] res = new Interval[totalSteps];

		final int offset = 0;

		final long[] min = new long[in.numDimensions()];
		final long[] pointCtr = new long[in.numDimensions()];
		final long[] srcDims = new long[in.numDimensions()];

		in.min(min);
		in.max(pointCtr);
		in.dimensions(srcDims);

		long[] max = pointCtr.clone();

		final int[] unselected = getUnselectedDimIndices(selected, srcDims.length);

		final long[] indicators = new long[unselected.length];
		final Interval interval = new FinalInterval(min, pointCtr);

		for (int j = indicators.length - 1; j > -1; j--) {
			indicators[j] = 1;
			if (j < indicators.length - 1) indicators[j] =
				(srcDims[unselected[j + 1]]) * indicators[j + 1];
		}

		for (final int u : unselected) {
			pointCtr[u] = -1;
		}

		for (int n = 0; n < getNumIterationSteps(selected, in); n++) {
			max = pointCtr.clone();

			for (int j = 0; j < indicators.length; j++) {
				if (n % indicators[j] == 0) pointCtr[unselected[j]]++;

				if (srcDims[unselected[j]] == pointCtr[unselected[j]]) pointCtr[unselected[j]] =
					0;
			}

			for (final int u : unselected) {
				max[u] = pointCtr[u] + min[u];
				min[u] = max[u];
			}

			res[offset + n] = new FinalInterval(min, max);
			interval.min(min);
		}
		return res;
	}

	/**
	 * @param dims
	 * @return
	 */
	public static final int getNumIterationSteps(final int[] selectedDims,
		final Interval interval)
	{

		final long[] dims = new long[interval.numDimensions()];
		interval.dimensions(dims);

		final int[] unselectedDims =
			getUnselectedDimIndices(selectedDims, dims.length);
		int steps = 1;
		for (int i = 0; i < unselectedDims.length; i++) {
			steps *= dims[unselectedDims[i]];
		}

		return steps;
	}

	/**
	 * @return
	 */
	public static final int[] getUnselectedDimIndices(final int[] selectedDims,
		final int numDims)
	{
		final boolean[] tmp = new boolean[numDims];
		int i;
		for (i = 0; i < selectedDims.length; i++) {
			if (selectedDims[i] >= numDims) {
				break;
			}
			tmp[selectedDims[i]] = true;
		}

		final int[] res = new int[numDims - i];

		int j = 0;
		for (int k = 0; j < res.length; k++) {
			if (k >= tmp.length || !tmp[k]) {
				res[j++] = k;
			}
		}
		return res;

	}
}
