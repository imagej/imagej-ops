
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "arccoth" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Arccoth.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Arccoth extends Op {

	String NAME = "arccoth";
}
