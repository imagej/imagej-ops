
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "arccsc" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Arccsc.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Arccsc extends Op {

	String NAME = "arccsc";
}
