
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Floor;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the floor of the real
 * component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Floor.NAME)
public class RealFloor<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Floor
{

	@Override
	public O compute(I x, O output) {
		output.setReal(Math.floor(x.getRealDouble()));
		return output;
	}

}
