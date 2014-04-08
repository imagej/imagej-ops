package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "log2" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Log2.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Log2 extends Op
{
	String NAME = "log2";
}
