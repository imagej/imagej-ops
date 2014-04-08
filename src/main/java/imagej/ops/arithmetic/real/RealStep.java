
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Step;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets an output real number to 0 if the input real number is less than 0.
 * Otherwise sets the output real number to 1. This implements a step function
 * similar to Mathematica's unitstep function. It is a Heaviside step function
 * with h(0) = 1 rather than 0.5.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Step.NAME)
public class RealStep<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Step
{

	@Override
	public O compute(I x, O output) {
		if (x.getRealDouble() < 0) output.setZero();
		else output.setOne();
		return output;
	}

}
