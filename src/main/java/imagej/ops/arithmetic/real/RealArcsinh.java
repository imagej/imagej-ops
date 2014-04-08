package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Arcsinh;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the inverse hyperbolic
 * sine of the real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = Arcsinh.NAME )
public class RealArcsinh< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements Arcsinh
{
	@Override
	public O compute( I x, O output )
	{
		double xt = x.getRealDouble();
		double delta = Math.sqrt( xt * xt + 1 );
		double value = Math.log( xt + delta );
		output.setReal( value );
		return output;
	}

}
