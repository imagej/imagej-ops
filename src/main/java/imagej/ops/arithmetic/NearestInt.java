package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "nearestint" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = NearestInt.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface NearestInt extends Op
{
	String NAME = "nearestint";
}
