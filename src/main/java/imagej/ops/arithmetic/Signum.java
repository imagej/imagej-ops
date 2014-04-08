
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "signum" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Signum.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Signum extends Op {

	String NAME = "signum";
}
