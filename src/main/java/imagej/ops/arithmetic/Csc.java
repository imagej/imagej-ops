package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "csc" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Csc.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Csc extends Op
{
	String NAME = "csc";
}
