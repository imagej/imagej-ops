
package imagej.ops.loop;

import imagej.ops.Function;

/**
 * Loops over an injected {@link Function}. A {@link LoopFunction} applies a
 * {@link Function} n-times to an input.
 * 
 * @author Christian Dietz
 */
public interface LoopFunction<I> extends Function<I, I>, Loop<I> {
	// NB: Marker interface
}
