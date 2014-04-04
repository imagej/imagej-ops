
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
public interface JoinInplaceAndInplace<A> extends
	JoinFunctionAndFunction<A, A, A, InplaceFunction<A>, InplaceFunction<A>>
{
	// NB: Marker interface.
}
