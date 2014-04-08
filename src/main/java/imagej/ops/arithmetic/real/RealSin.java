
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Sin;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the sine of the real
 * component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Sin.NAME)
public class RealSin<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Sin
{

	@Override
	public O compute(I x, O output) {
		double value = Math.sin(x.getRealDouble());
		output.setReal(value);
		return output;
	}

}
