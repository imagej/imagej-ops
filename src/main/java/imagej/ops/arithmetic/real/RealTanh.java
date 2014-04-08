
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Tanh;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the hyperbolic tangent of
 * the real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Tanh.NAME)
public class RealTanh<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Tanh
{

	@Override
	public O compute(I x, O output) {
		double value = Math.tanh(x.getRealDouble());
		output.setReal(value);
		return output;
	}

}
