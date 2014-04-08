package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "minconstant" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = MinConstant.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface MinConstant extends Op
{
	String NAME = "minconstant";
}
