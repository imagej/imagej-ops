package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "log10" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Log10.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Log10 extends Op
{
	String NAME = "log10";
}
