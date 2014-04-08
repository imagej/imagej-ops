
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "divideconstant" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = DivideConstant.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface DivideConstant extends Op {

	String NAME = "divideconstant";
}
