
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Reciprocal;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the reciprocal of the
 * real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Reciprocal.NAME)
public class RealReciprocal<I extends RealType<I>, O extends RealType<O>>
	extends AbstractFunction<I, O> implements Reciprocal
{

	private final double dbzVal;

	public RealReciprocal(double dbzVal) {
		this.dbzVal = dbzVal;
	}

	@Override
	public O compute(I x, O output) {
		double inputVal = x.getRealDouble();
		if (inputVal == 0) output.setReal(dbzVal);
		else output.setReal(1.0 / inputVal);
		return output;
	}

}
