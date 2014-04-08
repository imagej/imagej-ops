
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Tan;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the tangent of the real
 * component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Tan.NAME)
public class RealTan<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Tan
{

	@Override
	public O compute(I x, O output) {
		double value = Math.tan(x.getRealDouble());
		output.setReal(value);
		return output;
	}

}
