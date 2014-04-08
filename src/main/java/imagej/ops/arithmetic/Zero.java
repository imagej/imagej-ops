package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "zero" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Zero.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Zero extends Op
{
	String NAME = "zero";
}
