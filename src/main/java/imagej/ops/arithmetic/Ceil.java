package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "ceil" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Ceil.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Ceil extends Op
{
	String NAME = "ceil";
}
