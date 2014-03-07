package imagej.ops.slicer;

import imagej.ops.Op;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;

public interface HyperSlicer extends Op {

	<T> RandomAccessibleInterval<T> hyperSlice(
			final RandomAccessibleInterval<T> in, final Interval i);
}
