
package imagej.ops.loop;

import imagej.ops.AbstractInplaceFunction;
import imagej.ops.Function;

import org.scijava.plugin.Parameter;

/**
 * Abstract implementation of a {@link InplaceLoop}.
 * 
 * @author Christian Dietz
 * @param <I>
 * @param <O>
 */
public abstract class AbstractInplaceLoop<I> extends AbstractInplaceFunction<I>
	implements InplaceLoop<I>
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
