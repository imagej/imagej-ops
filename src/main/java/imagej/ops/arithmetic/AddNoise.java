package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "addnoise" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = AddNoise.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface AddNoise extends Op
{
	String NAME = "addnoise";
}
