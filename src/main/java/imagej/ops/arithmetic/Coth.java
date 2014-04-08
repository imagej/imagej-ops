package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "coth" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Coth.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Coth extends Op
{
	String NAME = "coth";
}
