
package imagej.ops.loop;

import imagej.ops.AbstractInplaceFunction;
import imagej.ops.Function;

import org.scijava.plugin.Parameter;

/**
 * Abstract implementation of a {@link LoopInplace}.
 * 
 * @author Christian Dietz
 */
public abstract class AbstractLoopInplace<I> extends AbstractInplaceFunction<I>
	implements LoopInplace<I>
{

	/**
	 * Number of iterations
	 */
	@Parameter
	protected Function<I, I> function;

	/**
	 * Number of iterations
	 */
	@Parameter
	protected int n;

	/**
	 * @return the {@link Function} to be applied
	 */
	@Override
	public Function<I, I> getFunction() {
		return function;
	}

	/**
	 * @param function to be applied
	 */
	@Override
	public void setFunction(final Function<I, I> function) {
		this.function = function;
	}
}
