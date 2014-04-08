package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "arctanh" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Arctanh.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Arctanh extends Op
{
	String NAME = "arctanh";
}
