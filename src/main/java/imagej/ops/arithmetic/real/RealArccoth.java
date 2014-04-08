
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Arccoth;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the inverse hyperbolic
 * cotangent of the real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Arccoth.NAME)
public class RealArccoth<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Arccoth
{

	@Override
	public O compute(I x, O output) {
		double xt = x.getRealDouble();
		double value = 0.5 * Math.log((xt + 1) / (xt - 1));
		output.setReal(value);
		return output;
	}

}
