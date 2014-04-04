
package imagej.ops.join;

import imagej.ops.Function;

/**
 * A join operation which joins two {@link Function}s. The resulting function
 * will take the input of the first {@link Function} as input and the output of
 * the second {@link Function} as the output.
 * 
 * @author Christian Dietz
 * @author Curtis Rueden
 */
public interface JoinFunctionAndFunction<A, B, C, F1 extends Function<A, B>, F2 extends Function<B, C>>
	extends Function<A, C>, Join
{

	/**
	 * @return first {@link Function} to be joined
	 */
	F1 getFirst();

	/**
	 * @param first {@link Function} to be joined
	 */
	void setFirst(F1 first);

	/**
	 * @return second {@link Function} to be joined
	 */
	F2 getSecond();

	/**
	 * @param second {@link Function} to be joined
	 */
	void setSecond(F2 second);

}
