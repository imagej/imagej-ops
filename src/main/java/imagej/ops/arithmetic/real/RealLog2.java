
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Log2;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the base 2 log of the
 * real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Log2.NAME)
public class RealLog2<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Log2
{

	@Override
	public O compute(I x, O output) {
		double value = Math.log(x.getRealDouble()) / Math.log(2);
		output.setReal(value);
		return output;
	}

}
