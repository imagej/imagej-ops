
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "sqrt" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Sqrt.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Sqrt extends Op {

	String NAME = "sqrt";
}
