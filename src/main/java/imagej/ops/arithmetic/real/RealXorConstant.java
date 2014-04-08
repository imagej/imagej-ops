
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.XorConstant;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the logical XOR of the
 * real component of an input real number with a constant value. The constant
 * value is specified in the constructor.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = XorConstant.NAME)
public class RealXorConstant<I extends RealType<I>, O extends RealType<O>>
	extends AbstractFunction<I, O> implements XorConstant
{

	private final long constant;

	public RealXorConstant(long constant) {
		this.constant = constant;
	}

	@Override
	public O compute(I x, O output) {
		long value = constant ^ (long) x.getRealDouble();
		output.setReal(value);
		return output;
	}

}
