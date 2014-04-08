
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Ulp;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the size of the ulp of an
 * input real number. An ulp of a floating point value is the positive distance
 * between an input floating-point value and the floating point value next
 * larger in magnitude. Note that for non-NaN x, ulp(-x) == ulp(x).
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Ulp.NAME)
public class RealUlp<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Ulp
{

	@Override
	public O compute(I x, O output) {
		double value = Math.ulp(x.getRealDouble());
		output.setReal(value);
		return output;
	}

}
