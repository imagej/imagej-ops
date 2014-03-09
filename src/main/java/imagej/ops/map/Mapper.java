
package imagej.ops.map;

import imagej.ops.Function;
import imagej.ops.Op;

/**
 * Simple marker interface. A
 * 
 * @author Christian Dietz
 */
public interface Mapper<A, B> extends Op {

	/**
	 * @return the {@link Function} to be mapped
	 */
	Function<A, B> getFunction();

	/**
	 * Set the {@link Function} to be mapped
	 */
	void setFunction(Function<A, B> func);
}
