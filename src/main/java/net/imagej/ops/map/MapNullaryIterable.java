
package net.imagej.ops.map;

import net.imagej.ops.Ops;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

/**
 * A {@link MapNullaryComputer} over an {@link Iterable}.
 * 
 * @author Leon Yang
 * @param <O> element type of outputs
 */
@Plugin(type = Ops.Map.class, priority = Priority.LOW_PRIORITY)
public class MapNullaryIterable<O> extends AbstractMapNullaryComputer<O, Iterable<O>> {

	@Override
	public void compute0(Iterable<O> output) {
		Maps.map(output, getOp());
	}

}
