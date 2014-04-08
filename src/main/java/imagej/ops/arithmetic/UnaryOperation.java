package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "unaryoperation" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = UnaryOperation.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface UnaryOperation extends Op
{
	String NAME = "unaryoperation";
}
