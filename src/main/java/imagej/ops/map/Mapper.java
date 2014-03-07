package imagej.ops.map;

import imagej.ops.Op;
import imagej.ops.UnaryFunction;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Basic Unary Function Threader which does not make any assumpation about the
 * iteration orders of the input
 * 
 * @author Christian Dietz
 * 
 * @param <A>
 * @param <B>
 */
@Plugin(type = Op.class, name = "map", priority = Priority.LOW_PRIORITY)
public class Mapper<A, B> implements Op {

	@Parameter
	private IterableInterval<A> in;

	@Parameter
	private UnaryFunction<A, B> func;

	@Parameter(type = ItemIO.BOTH)
	private RandomAccessibleInterval<B> out;

	@Override
	public void run() {

		final Cursor<A> cursor = in.cursor();
		final RandomAccess<B> rndAccess = out.randomAccess();

		while (cursor.hasNext()) {
			cursor.fwd();
			rndAccess.setPosition(cursor);
			func.compute(cursor.get(), rndAccess.get());
		}
	}
}
