
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "arccsch" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Arccsch.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Arccsch extends Op {

	String NAME = "arccsch";
}
