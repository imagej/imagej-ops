package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "negate" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Negate.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Negate extends Op
{
	String NAME = "negate";
}
