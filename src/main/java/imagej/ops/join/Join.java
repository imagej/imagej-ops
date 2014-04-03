
package imagej.ops.join;

/**
 * Base interface for "join" operations.
 * <p>
 * Implementing classes should be annotated with:
 * </p>
 * 
 * <pre>
 * @Plugin(type = Op.class, name = Join.NAME)
 * </pre>
 * 
 * @author Curtis Rueden
 */
public interface Join {

	String NAME = "join";

}
