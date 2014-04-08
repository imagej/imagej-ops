
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "arccosh" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Arccosh.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Arccosh extends Op {

	String NAME = "arccosh";
}
