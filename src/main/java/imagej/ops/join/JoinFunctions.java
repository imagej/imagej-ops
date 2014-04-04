
package imagej.ops.join;

import imagej.ops.Function;
import imagej.ops.OutputFactory;

import java.util.List;

/**
 * A join operation which joins a list of {@link Function}s.
 * 
 * @author Christian Dietz
 * @author Curtis Rueden
 */
public interface JoinFunctions<A, F extends Function<A, A>> extends
	Function<A, A>, Join
{

	/**
	 * @return {@link OutputFactory} used to create intermediate results
	 */
	OutputFactory<A, A> getBufferFactory();

	/**
	 * Sets the {@link OutputFactory} which is used to create intermediate
	 * results.
	 * 
	 * @param bufferFactory used to create intermediate results
	 */
	void setBufferFactory(OutputFactory<A, A> bufferFactory);

	/**
	 * @return {@link List} of {@link Function}s which are joined in this
	 *         {@link Join}
	 */
	List<? extends F> getFunctions();

	/**
	 * Sets the {@link Function}s which are joined in this {@link Join}.
	 * 
	 * @param functions joined in this {@link Join}
	 */
	void setFunctions(List<? extends F> functions);

}
