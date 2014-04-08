
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Arctanh;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the inverse hyperbolic
 * tangent of the real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Arctanh.NAME)
public class RealArctanh<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Arctanh
{

	@Override
	public O compute(I x, O output) {
		double xt = x.getRealDouble();
		double value = 0.5 * Math.log((1 + xt) / (1 - xt));
		output.setReal(value);
		return output;
	}

}
