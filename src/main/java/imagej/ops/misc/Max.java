
package imagej.ops.misc;

import imagej.ops.Function;

/**
 * Simple marker interface
 * 
 * @author Christian Dietz
 * @param <T>
 * @param <V>
 */
public interface Max<T, V> extends Function<Iterable<T>, V> {
	// NB: Marker for Maximum Operations
}
