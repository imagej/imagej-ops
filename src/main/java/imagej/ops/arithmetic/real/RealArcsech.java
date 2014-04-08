
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Arcsech;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the inverse hyperbolic
 * secant of the real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Arcsech.NAME)
public class RealArcsech<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Arcsech
{

	@Override
	public O compute(I x, O output) {
		double xt = x.getRealDouble();
		double numer = 1 + Math.sqrt(1 - xt * xt);
		double value = Math.log(numer / xt);
		output.setReal(value);
		return output;
	}

}
