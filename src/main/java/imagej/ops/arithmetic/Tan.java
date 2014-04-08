package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "tan" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Tan.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Tan extends Op
{
	String NAME = "tan";
}
