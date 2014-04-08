
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Abs;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the absolute value of the
 * real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Abs.NAME)
public class RealAbs<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Abs
{

	@Override
	public O compute(I x, O output) {
		output.setReal(Math.abs(x.getRealDouble()));
		return output;
	}

}
