package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "sincpi" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = SincPi.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface SincPi extends Op
{
	String NAME = "sincpi";
}
