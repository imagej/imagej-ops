
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "logoneplusx" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = LogOnePlusX.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface LogOnePlusX extends Op {

	String NAME = "logoneplusx";
}
