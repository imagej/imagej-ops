
package imagej.ops.loop;

import imagej.ops.AbstractFunction;
import imagej.ops.Function;

import org.scijava.plugin.Parameter;

/**
 * Abstract implementation of a {@link FunctionLoop}.
 * 
 * @author Christian Dietz
 * @param <I>
 * @param <O>
 */
public abstract class AbstractFunctionLoop<F extends Function<I, I>, I> extends
	AbstractFunction<I, I> implements FunctionLoop<I>
{

	/**
	 * Number of iterations
	 */
	@Parameter
	protected Function<I, I> function;

	/**
	 * Buffer for intermediate results
	 */
	@Parameter(required = false)
	protected I buffer;

	/**
	 * Number of iterations
	 */
	@Parameter
	protected int n;

	/**
	 * @return the {@link Function} to be applied
	 */
	public Function<I, I> getFunction() {
		return function;
	}

	/**
	 * @param function to be applied
	 */
	public void setFunction(final Function<I, I> function) {
		this.function = function;
	}
}
