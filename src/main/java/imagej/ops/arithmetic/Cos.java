package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "cos" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Cos.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Cos extends Op
{
	String NAME = "cos";
}
