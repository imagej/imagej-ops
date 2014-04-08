package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "andconstant" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = AndConstant.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface AndConstant extends Op
{
	String NAME = "andconstant";
}
