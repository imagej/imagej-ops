
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "orconstant" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = OrConstant.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface OrConstant extends Op {

	String NAME = "orconstant";
}
