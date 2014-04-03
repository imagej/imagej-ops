
package imagej.ops.join;

import imagej.ops.Function;
import imagej.ops.InplaceFunction;

/**
 * A join operation which joins two {@link InplaceFunction}s. The resulting
 * function will take the input of the first {@link Function} as input and the
 * output of the second {@link Function} as the output;
 * 
 * @author Christian Dietz
 */
public interface JoinInplace<A> extends Join {

	/**
	 * @return first {@link Function} to be joined
	 */
	InplaceFunction<A> getFirst();

	/**
	 * @param first {@link Function} to be joined
	 */
	void setFirst(InplaceFunction<A> first);

	/**
	 * @return second {@link Function} to be joined
	 */
	InplaceFunction<A> getSecond();

	/**
	 * @param second {@link Function} to be joined
	 */
	void setSecond(InplaceFunction<A> second);

}
