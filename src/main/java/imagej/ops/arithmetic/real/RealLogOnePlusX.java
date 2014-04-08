package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.LogOnePlusX;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the natural logarithm of
 * the sum of the argument and 1. This calculation is more accurate than
 * explicitly calling log(1.0 + x).
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = LogOnePlusX.NAME )
public class RealLogOnePlusX< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements LogOnePlusX
{
	@Override
	public O compute( I x, O output )
	{
		double value = Math.log1p( x.getRealDouble() );
		output.setReal( value );
		return output;
	}

}
