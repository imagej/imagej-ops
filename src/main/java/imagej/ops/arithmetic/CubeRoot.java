
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "cuberoot" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = CubeRoot.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface CubeRoot extends Op {

	String NAME = "cuberoot";
}
