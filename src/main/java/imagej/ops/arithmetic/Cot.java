package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "cot" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Cot.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Cot extends Op
{
	String NAME = "cot";
}
