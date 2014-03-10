
package imagej.ops.loop;

import imagej.ops.AbstractInplaceFunction;
import imagej.ops.Function;
import imagej.ops.InplaceFunction;

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
	protected InplaceFunction<I> function;

	/**
	 * Number of iterations
	 */
	@Parameter
	protected int n;

	/**
	 * @return the {@link Function} to be applied
	 */
	public InplaceFunction<I> getFunction() {
		return function;
	}

	/**
	 * @param function to be applied
	 */
	public void setFunction(final InplaceFunction<I> function) {
		this.function = function;
	}
}
