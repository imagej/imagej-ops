
package imagej.ops.loop;

import imagej.ops.InplaceFunction;

/**
 * Loops over an injected {@link InplaceFunction}. A {@link LoopInplace} applies
 * a {@link InplaceFunction} n-times to an input. Note: input will be modified!
 * 
 * @author Christian Dietz
 */
public interface LoopInplace<I> extends InplaceFunction<I>, Loop<I> {
	// NB: Marker interface
}
