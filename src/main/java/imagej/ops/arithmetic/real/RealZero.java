
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Zero;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to zero.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Zero.NAME)
public class RealZero<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Zero
{

	@Override
	public O compute(I x, O output) {
		output.setZero();
		return output;
	}

}
