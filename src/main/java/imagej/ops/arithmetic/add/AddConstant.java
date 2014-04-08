package imagej.ops.arithmetic.add;

import imagej.ops.Op;

/**
 * Base interface for "addconstant" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = AddConstant.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface AddConstant extends Op
{
	String NAME = "addconstant";
}
