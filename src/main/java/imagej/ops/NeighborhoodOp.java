package imagej.ops;

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
public class NeighborhoodOp<I, O> implements Op {

	@Parameter
	private OpService service;

	@Parameter
	private Shape shape;

	@Parameter
	private RandomAccessibleInterval<I> in;

	@Parameter(type = ItemIO.BOTH)
	private RandomAccessibleInterval<O> out;

	@Parameter
	private UnaryFunction<Iterable<I>, O> func;

	@Override
	public void run() {
		service.run("map", shape.neighborhoodsSafe(in), out, func);
	}
}
