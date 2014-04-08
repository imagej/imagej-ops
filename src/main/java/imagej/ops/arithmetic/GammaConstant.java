package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "gammaconstant" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = GammaConstant.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface GammaConstant extends Op
{
	String NAME = "gammaconstant";
}
