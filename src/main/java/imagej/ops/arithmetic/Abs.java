package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "abs" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Abs.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Abs extends Op
{
	String NAME = "abs";
}
