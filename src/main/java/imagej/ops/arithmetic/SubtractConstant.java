package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "subtractconstant" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = SubtractConstant.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface SubtractConstant extends Op
{
	String NAME = "subtractconstant";
}
