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
 * @author Christian Dietz
 * 
 * @param <A>
 * @param <B>
 */
@Plugin(type = Op.class, name = "map", priority = Priority.LOW_PRIORITY + 1)
public class ThreadedMapper<A, B> extends AbstractThreadedMapper {

	@Parameter
	private IterableInterval<A> in;

	@Parameter
	private UnaryFunction<A, B> func;

	@Parameter(type = ItemIO.BOTH)
	private RandomAccessibleInterval<B> out;

	@Override
	protected void runThread(final int firstElement, final int lastElement) {
		final Cursor<A> cursor = in.cursor();
		cursor.jumpFwd(firstElement - 1);

		final RandomAccess<B> rndAccess = out.randomAccess();
		final UnaryFunction<A, B> copy = func.copy();

		int ctr = 0;
		while (cursor.hasNext() && ctr < lastElement + 1) {
			cursor.fwd();
			rndAccess.setPosition(cursor);
			copy.compute(cursor.get(), rndAccess.get());
			ctr++;
		}
	}

	@Override
	public void run() {
		runThreading(in.size());
	}
}
