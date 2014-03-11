
package imagej.ops.loop;

import imagej.ops.Function;

/**
 * Base interface for "loop" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Loop.NAME)
 * </pre>
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
