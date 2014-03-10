
package imagej.ops.loop;

import imagej.ops.InplaceFunction;

/**
 * Loops over an injected {@link InplaceFunction}. A {@link InplaceLoop} applies
 * a {@link InplaceFunction} n-times to an input. Note: input will be modified!
 * 
 * @author Christian Dietz
 */
public interface InplaceLoop<I> extends InplaceFunction<I>, Loop {
	// NB: Marker interface
}
