
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Exp;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the exponentiation of the
 * real component of an input real number. (e raised to a power)
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Exp.NAME)
public class RealExp<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Exp
{

	@Override
	public O compute(I x, O output) {
		double value = Math.exp(x.getRealDouble());
		output.setReal(value);
		return output;
	}

}
