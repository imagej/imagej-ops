
package imagej.ops.slicer;

import imagej.ops.Op;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "hyperslicer", priority = Priority.LOW_PRIORITY)
public class RandomAccessibleIntervalSlicer<T extends Type<T>> extends
	AbstractHyperSlicer
{

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
