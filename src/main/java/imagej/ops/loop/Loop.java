
package imagej.ops.loop;

import imagej.ops.Function;

/**
 * Marker interface
 * 
 * @author Christian Dietz
 */
public interface Loop<A> {

	public final static String NAME = "loop";

	/**
	 * @return the {@link Function} used for looping
	 */
	Function<A, A> getFunction();

	/**
	 * @param func the {@link Function} used for looping
	 */
	void setFunction(Function<A, A> func);

}
