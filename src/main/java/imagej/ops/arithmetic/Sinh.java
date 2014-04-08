
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "sinh" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Sinh.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Sinh extends Op {

	String NAME = "sinh";
}
