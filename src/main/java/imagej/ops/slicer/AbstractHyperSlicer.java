package imagej.ops.slicer;

import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.util.Intervals;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

public abstract class AbstractHyperSlicer implements HyperSlicer {

	@Override
	public <T> RandomAccessibleInterval<T> hyperSlice(
			final RandomAccessibleInterval<T> in, final Interval i) {
		boolean oneSizedDims = false;

		for (int d = 0; d < in.numDimensions(); d++) {
			if (in.dimension(d) == 1) {
				oneSizedDims = true;
				break;
			}
		}

		if (Intervals.equals(in, i) && !oneSizedDims)
			return in;

		IntervalView<T> res;
		if (Intervals.contains(in, i))
			res = Views.offsetInterval(in, i);
		else {
			throw new RuntimeException(
					"Interval must fit into src in SubsetViews.subsetView(...)");
		}

		for (int d = i.numDimensions() - 1; d >= 0; --d)
			if (i.dimension(d) == 1 && res.numDimensions() > 1)
				res = Views.hyperSlice(res, d, 0);

		return res;
	}

}
