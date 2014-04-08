
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Copy;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the real component of an
 * input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Copy.NAME)
public class RealCopy<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Copy
{

	@Override
	public O compute(I x, O output) {
		output.setReal(x.getRealDouble());
		return output;
	}

}
