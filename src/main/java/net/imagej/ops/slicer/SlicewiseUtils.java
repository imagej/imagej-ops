package net.imagej.ops.slicer;

import net.imglib2.meta.AxisType;
import net.imglib2.meta.TypedSpace;

public class SlicewiseUtils {

	/**
	 * @param input
	 *            for which the {@link AxisType}s indices will be determined
	 * @param axisTypes
	 *            which will be used to determine the indices
	 * @return
	 */
	public static synchronized int[] getAxesIndices(final TypedSpace<?> input,
			final AxisType[] axisTypes) {
		if (axisTypes == null)
			return null;

		int[] indices = new int[axisTypes.length];

		for (int i = 0; i < axisTypes.length; i++) {
			indices[i] = input.dimensionIndex(axisTypes[i]);

			if (indices[i] == -1) {
				// TODO nicer exception handling
				throw new IllegalArgumentException(
						"AxisType not available in TypedSpace<?>");
			}
		}

		return indices;
	}

}
