package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "floor" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Floor.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Floor extends Op
{
	String NAME = "floor";
}
