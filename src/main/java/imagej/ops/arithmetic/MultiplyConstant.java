package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "multiplyconstant" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = MultiplyConstant.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface MultiplyConstant extends Op
{
	String NAME = "multiplyconstant";
}
