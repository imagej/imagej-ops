
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "arccot" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Arccot.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Arccot extends Op {

	String NAME = "arccot";
}
