
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "log" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Log.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Log extends Op {

	String NAME = "log";
}
