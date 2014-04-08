
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Arccot;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the inverse cotangent of
 * the real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Arccot.NAME)
public class RealArccot<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Arccot
{

	@Override
	public O compute(I x, O output) {
		double value = Math.atan(1.0 / x.getRealDouble());
		if (x.getRealDouble() < 0) value += Math.PI;
		output.setReal(value);
		return output;
	}

}
