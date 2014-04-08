
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Log10;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the 10-based log of the
 * real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Log10.NAME)
public class RealLog10<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Log10
{

	@Override
	public O compute(I x, O output) {
		double value = Math.log10(x.getRealDouble());
		output.setReal(value);
		return output;
	}

}
