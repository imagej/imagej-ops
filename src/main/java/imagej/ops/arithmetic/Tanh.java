package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "tanh" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Tanh.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Tanh extends Op
{
	String NAME = "tanh";
}
