
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "arctan" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Arctan.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Arctan extends Op {

	String NAME = "arctan";
}
