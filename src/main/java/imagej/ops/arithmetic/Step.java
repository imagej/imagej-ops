package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "step" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Step.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Step extends Op
{
	String NAME = "step";
}
