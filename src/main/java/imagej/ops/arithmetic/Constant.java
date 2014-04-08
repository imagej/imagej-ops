
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "constant" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Constant.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Constant extends Op {

	String NAME = "constant";
}
