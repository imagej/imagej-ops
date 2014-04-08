package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "cosh" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Cosh.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Cosh extends Op
{
	String NAME = "cosh";
}
