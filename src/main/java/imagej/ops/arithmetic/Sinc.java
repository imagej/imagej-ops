package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "sinc" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Sinc.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Sinc extends Op
{
	String NAME = "sinc";
}
