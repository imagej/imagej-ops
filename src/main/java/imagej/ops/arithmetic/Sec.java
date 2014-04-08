
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "sec" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Sec.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Sec extends Op {

	String NAME = "sec";
}
