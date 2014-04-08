package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "powerconstant" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = PowerConstant.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface PowerConstant extends Op
{
	String NAME = "powerconstant";
}
