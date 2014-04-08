
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.add.AddConstant;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the addition of the real
 * component of an input real number with a constant value. The constant value
 * is specified in the constructor.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = AddConstant.NAME)
public class RealAddConstant<I extends RealType<I>, O extends RealType<O>>
	extends AbstractFunction<I, O> implements AddConstant
{

	private final double constant;

	public RealAddConstant(double constant) {
		this.constant = constant;
	}

	@Override
	public O compute(I x, O output) {
		double value = x.getRealDouble() + constant;
		output.setReal(value);
		return output;
	}

}
