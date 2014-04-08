
package imagej.ops.arithmetic.real;

import java.util.Random;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.GaussianRandom;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to a random value using a
 * gaussian distribution. The input value is considered the standard deviation
 * of the desired distribution and must be positive. The output value has mean
 * value 0.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = GaussianRandom.NAME)
public class RealGaussianRandom<I extends RealType<I>, O extends RealType<O>>
	extends AbstractFunction<I, O> implements GaussianRandom
{

	private Random rng = new Random();

	@Override
	public O compute(I x, O output) {
		double value = rng.nextGaussian() * Math.abs(x.getRealDouble());
		output.setReal(value);
		return output;
	}

}
