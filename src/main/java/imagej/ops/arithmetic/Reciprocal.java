
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "reciprocal" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Reciprocal.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Reciprocal extends Op {

	String NAME = "reciprocal";
}
