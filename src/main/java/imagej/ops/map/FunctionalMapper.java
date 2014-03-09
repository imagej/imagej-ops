
package imagej.ops.map;

/**
 * Simple marker interface, marking a {@link FunctionalMapper}.
 * 
 * @author Christian Dietz
 * @param <A>
 * @param <B>
 */
public interface FunctionalMapper<A, B> extends Mapper<A, B> {
	// NB: Marker
}
