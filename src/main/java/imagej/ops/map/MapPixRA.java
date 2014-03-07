
package imagej.ops.map;

import imagej.ops.Op;
import imagej.ops.UnaryFunction;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Applies a {@link UnaryFunction} to each pixel of an {@link IterableInterval}
 * and writes it into another {@link RandomAccessibleInterval}. Comes into play
 * if the iteration orders of each input image are not equal.
 */
@Plugin(type = Op.class, name = "map", priority = Priority.LOW_PRIORITY)
public class MapPixRA<S extends RealType<S>, T extends RealType<T>> implements
	Op
{

	@Parameter
	private IterableInterval<S> in;

	@Parameter
	UnaryFunction<S, T> func;

	@Parameter(type = ItemIO.BOTH)
	private RandomAccessibleInterval<T> out;

	@Override
	public void run() {

		final Cursor<S> inCur = in.cursor();
		final RandomAccess<T> outRA = out.randomAccess();
		while (inCur.hasNext()) {
			inCur.next();
			outRA.setPosition(inCur);
			func.compute(inCur.get(), outRA.get());
		}
	}

}
