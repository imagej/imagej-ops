
package imagej.ops.misc;

import imagej.ops.Op;

import java.util.Iterator;

import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Calculates the minimum and maximum value of an image.
 */
@Plugin(type = Op.class, name = "minmax")
public class MinMax<T extends RealType<T>> implements Op {

	@Parameter
	private Iterable<T> img;

	@Parameter(type = ItemIO.OUTPUT)
	private T min;

	@Parameter(type = ItemIO.OUTPUT)
	private T max;

	@Override
	public void run() {
		min = img.iterator().next().createVariable();
		max = min.copy();

		min.setReal(min.getMaxValue());
		max.setReal(max.getMinValue());

		final Iterator<T> it = img.iterator();
		while (it.hasNext()) {
			final T i = it.next();
			if (min.compareTo(i) > 0) min.set(i);
			if (max.compareTo(i) < 0) max.set(i);
		}
	}

}
