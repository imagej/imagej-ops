
package imagej.ops.map;

/**
 * Marker interface to mark {@link InplaceMapper}s
 * 
 * @author Christian Dietz
 */
public interface InplaceMapper<A> extends Mapper<A, A> {
	// NB: Marker interface
}
