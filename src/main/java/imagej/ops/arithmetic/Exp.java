package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "exp" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Exp.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Exp extends Op
{
	String NAME = "exp";
}
