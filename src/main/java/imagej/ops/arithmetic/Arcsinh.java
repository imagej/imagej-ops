package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "arcsinh" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Arcsinh.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Arcsinh extends Op
{
	String NAME = "arcsinh";
}
