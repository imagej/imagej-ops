package imagej.ops;

import imagej.Cancelable;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.region.localneighborhood.Neighborhood;
import net.imglib2.algorithm.region.localneighborhood.Shape;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public class NeighborhoodOp<I, O> implements Op, Cancelable {

	@Parameter
	private RandomAccessibleInterval<I> in;

	@Parameter
	private Shape shape;

	@Parameter
	private UnaryFunction<Iterable<I>, O> func;

	@Parameter(type = ItemIO.BOTH)
	private RandomAccessibleInterval<O> out;

	private final String cancelationReason = null;

	@Override
	public void run() {

		// TODO make a faster op where iteration order of in and out are the
		// same
		final RandomAccess<O> rnd = out.randomAccess();
		
		final IterableInterval<Neighborhood<I>> neighborhood = shape
				.neighborhoods(in);
		
		
		
		for (final Neighborhood<I> neighborhood : s) {
			rnd.setPosition(neighborhood);

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
