package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "invert" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Invert.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Invert extends Op
{
	String NAME = "invert";
}
