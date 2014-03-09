
package imagej.ops.map;

import imagej.ops.Function;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;

public abstract class AbstractInplaceMapper<A, I extends Iterable<A>> implements InplaceMapper<A> {

	@Parameter(type = ItemIO.BOTH)
	protected I in;

	@Parameter
	protected Function<A, A> func;

	@Override
	public Function<A, A> getFunction() {
		return func;
	}

	@Override
	public void setFunction(final Function<A, A> func) {
		this.func = func;
	}

}
