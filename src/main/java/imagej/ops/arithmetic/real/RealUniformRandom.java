
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.UniformRandom;

import java.util.Random;

import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to a random value between 0
 * and (input real number).
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = UniformRandom.NAME)
public class RealUniformRandom<I extends RealType<I>, O extends RealType<O>>
	extends AbstractFunction<I, O> implements UniformRandom
{

	private Random rng = new Random();

	@Override
	public O compute(I x, O output) {
		double r = rng.nextDouble();
		double value = r * x.getRealDouble();
		output.setReal(value);
		return output;
	}

}
