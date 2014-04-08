
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Sqr;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the square of the real
 * component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Sqr.NAME)
public class RealSqr<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Sqr
{

	@Override
	public O compute(I x, O output) {
		double value = x.getRealDouble();
		output.setReal(value * value);
		return output;
	}

}
