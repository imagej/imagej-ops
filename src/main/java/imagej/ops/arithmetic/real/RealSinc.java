package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Sinc;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the sinc value of the
 * real component of an input real number. The sinc function is defined as
 * sin(x) / x.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = Sinc.NAME )
public class RealSinc< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements Sinc
{
	@Override
	public O compute( I input, O output )
	{
		double x = input.getRealDouble();
		double value;
		if ( x == 0 )
			value = 1;
		else
			value = Math.sin( x ) / x;
		output.setReal( value );
		return output;
	}

}
