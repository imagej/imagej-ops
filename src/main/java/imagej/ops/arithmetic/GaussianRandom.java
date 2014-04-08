
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "gaussianrandom" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = GaussianRandom.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface GaussianRandom extends Op {

	String NAME = "gaussianrandom";
}
