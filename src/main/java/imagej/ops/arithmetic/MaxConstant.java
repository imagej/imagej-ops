
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "maxconstant" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = MaxConstant.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface MaxConstant extends Op {

	String NAME = "maxconstant";
}
