
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "sin" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Sin.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Sin extends Op {

	String NAME = "sin";
}
