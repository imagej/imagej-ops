package imagej.ops.slicer;

import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public class RandomAccessibleIntervalSlicer<T extends Type<T>> extends
		AbstractHyperSlicer {

	@Parameter
	Interval interval;

	@Parameter
	RandomAccessibleInterval<?> in;

	@Parameter(type = ItemIO.OUTPUT)
	RandomAccessibleInterval<?> out;

	@Override
	public void run() {
		out = hyperSlice(in, interval);
	}
}
