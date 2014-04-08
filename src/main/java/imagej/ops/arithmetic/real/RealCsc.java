
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Csc;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the cosecant of the real
 * component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Csc.NAME)
public class RealCsc<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Csc
{

	@Override
	public O compute(I x, O output) {
		double value = 1.0 / Math.sin(x.getRealDouble());
		output.setReal(value);
		return output;
	}

}
