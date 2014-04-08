
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "copy" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Copy.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Copy extends Op {

	String NAME = "copy";
}
