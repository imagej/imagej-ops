package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "ulp" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Ulp.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Ulp extends Op
{
	String NAME = "ulp";
}
