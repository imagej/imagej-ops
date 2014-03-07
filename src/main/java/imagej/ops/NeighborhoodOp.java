package imagej.ops;

import imagej.Cancelable;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.region.localneighborhood.Neighborhood;
import net.imglib2.algorithm.region.localneighborhood.Shape;

import org.scijava.ItemIO;
import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Evaluates an {@link UnaryFunction} for each {@link Neighborhood} on the in
 * {@link RandomAccessibleInterval}.
 * 
 * @author Christian Dietz
 * 
 */
@Plugin(type = Op.class, priority = Priority.LOW_PRIORITY)
public class NeighborhoodOp<I, O> implements Op, Cancelable {

	@Parameter
	private RandomAccessibleInterval<I> in;

	@Parameter
	private Shape shape;

	@Parameter
	private UnaryFunction<Iterable<I>, O> func;

	@Parameter(type = ItemIO.BOTH)
	private RandomAccessibleInterval<O> out;

	// used in cancellation service
	private final String cancelationReason = null;

	@Override
	public void run() {

		final RandomAccess<O> rnd = out.randomAccess();

		for (final Neighborhood<I> neighborhood : shape.neighborhoods(in)) {
			rnd.setPosition(neighborhood);
			func.compute(neighborhood, rnd.get());
		}
	}

	@Override
	public String getCancelReason() {
		return cancelationReason;
	}

	@Override
	public boolean isCanceled() {
		return cancelationReason != null;
	}

}
