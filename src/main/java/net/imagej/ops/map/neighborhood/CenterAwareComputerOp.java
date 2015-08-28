
package net.imagej.ops.map.neighborhood;

import net.imagej.ops.ComputerOp;
import net.imglib2.util.Pair;

/**
 * A <em>center aware computer</em> calculates a result from a given input and
 * its surrounding neighborhood, storing it into the specified output reference.
 * 
 * @author Jonathan Hale (University of Konstanz)
 * @param <I> type of input
 * @param <O> type of output
 */
public interface CenterAwareComputerOp<I, O> extends
	ComputerOp<Pair<I, Iterable<I>>, O>
{
	// NB: Marker interface.
}
