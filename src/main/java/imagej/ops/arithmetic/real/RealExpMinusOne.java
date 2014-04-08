package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.ExpMinusOne;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to e^x - 1. x is the input
 * argument to the operation.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = ExpMinusOne.NAME )
public class RealExpMinusOne< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements ExpMinusOne
{
	@Override
	public O compute( I x, O output )
	{
		double value = Math.exp( x.getRealDouble() ) - 1;
		output.setReal( value );
		return output;
	}

}
