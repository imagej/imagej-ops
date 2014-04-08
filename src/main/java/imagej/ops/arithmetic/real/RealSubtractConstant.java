
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.SubtractConstant;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the subtraction from the
 * real component of an input real number a constant value. The constant value
 * is specified in the constructor.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = SubtractConstant.NAME)
public class RealSubtractConstant<I extends RealType<I>, O extends RealType<O>>
	extends AbstractFunction<I, O> implements SubtractConstant
{

	private final double constant;

	public RealSubtractConstant(double constant) {
		this.constant = constant;
	}

	@Override
	public O compute(I x, O output) {
		double value = x.getRealDouble() - constant;
		output.setReal(value);
		return output;
	}

}
