
package imagej.ops.map;

import imagej.ops.AbstractFunction;
import imagej.ops.Function;

import org.scijava.plugin.Parameter;

/**
 * Abstract implemenation of a functional mapper.
 * 
 * @author Christian Dietz
 * @param <A>
 * @param <B>
 */
public abstract class AbstractFunctionalMapper<A, B, C, D> extends
	AbstractFunction<C, D> implements FunctionalMapper<A, B>
{

	@Parameter
	protected Function<A, B> func;

	@Override
	public Function<A, B> getFunction() {
		return func;
	}

	@Override
	public void setFunction(final Function<A, B> func) {
		this.func = func;
	}
}
