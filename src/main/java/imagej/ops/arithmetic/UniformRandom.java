package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "uniformrandom" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = UniformRandom.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface UniformRandom extends Op
{
	String NAME = "uniformrandom";
}
