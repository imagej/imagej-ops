
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Sinh;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the hyperbolic sine of
 * the real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Sinh.NAME)
public class RealSinh<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Sinh
{

	@Override
	public O compute(I x, O output) {
		double value = Math.sinh(x.getRealDouble());
		output.setReal(value);
		return output;
	}

}
