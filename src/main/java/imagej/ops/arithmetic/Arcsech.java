
package imagej.ops.arithmetic;

import imagej.ops.Op;

/**
 * Base interface for "arcsech" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Arcsech.NAME)
 * </pre>
 * 
 * @author Jonathan Hale
 * @author Christian Dietz
 */
public interface Arcsech extends Op {

	String NAME = "arcsech";
}
