
package imagej.ops.join;

import imagej.ops.Function;

import java.util.List;

/**
 * Base interface for helper classes which join {@link Function}s.
 * 
 * @author Christian Dietz
 * @author Curtis Rueden
 */
public interface FunctionJoiner<A, F extends Function<A, A>> {

	A getBuffer();

	void setBuffer(A buffer);

	List<? extends F> getFunctions();

	void setFunctions(List<? extends F> functions);

}
