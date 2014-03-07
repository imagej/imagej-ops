package imagej.ops.map;

import imagej.ops.Contingent;
import imagej.ops.Op;
import imagej.ops.UnaryFunction;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * @author Martin Horn
 * @author Christian Dietz
 * 
 * @param <A>
 * @param <B>
 */
@Plugin(type = Op.class, name = "map", priority = Priority.LOW_PRIORITY)
public class MapperII<A, B> implements Op, Contingent {

	@Parameter
	private IterableInterval<A> in;

	@Parameter
	private UnaryFunction<A, B> func;

	@Parameter(type = ItemIO.BOTH)
	private IterableInterval<B> out;

	@Override
	public void run() {
		final Cursor<A> inCursor = in.cursor();
		final Cursor<B> outCursor = out.cursor();

		while (inCursor.hasNext()) {
			inCursor.fwd();
			outCursor.fwd();
			func.compute(inCursor.get(), outCursor.get());
		}
	}

	@Override
	public boolean conforms() {
		return in.iterationOrder().equals(out.iterationOrder());
	}
}
