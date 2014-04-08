
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "csch" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Csch.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Csch extends Op {

	String NAME = "csch";
}
