package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.SincPi;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the sinc (pi version) of
 * the real component of an input real number. The pi version of sinc is defined
 * as sin(x*pi) / (x*pi).
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = SincPi.NAME )
public class RealSincPi< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements SincPi
{
	@Override
	public O compute( I input, O output )
	{
		double x = input.getRealDouble();
		double value;
		if ( x == 0 )
			value = 1;
		else
			value = Math.sin( Math.PI * x ) / ( Math.PI * x );
		output.setReal( value );
		return output;
	}

}
