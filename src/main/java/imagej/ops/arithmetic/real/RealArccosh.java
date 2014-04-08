
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Arccosh;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the inverse hyperbolic
 * cosine of the real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Arccosh.NAME)
public class RealArccosh<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Arccosh
{

	@Override
	public O compute(I x, O output) {
		double xt = x.getRealDouble();
		double delta = Math.sqrt(xt * xt - 1);
		if (xt <= -1) delta = -delta;
		double value = Math.log(xt + delta);
		output.setReal(value);
		return output;
	}

}
