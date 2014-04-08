package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Arccsch;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the inverse hyperbolic
 * cosecant of the real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = Arccsch.NAME )
public class RealArccsch< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements Arccsch
{
	@Override
	public O compute( I x, O output )
	{
		double xt = x.getRealDouble();
		double delta = Math.sqrt( 1 + ( 1 / ( xt * xt ) ) );
		double value = Math.log( ( 1 / xt ) + delta );
		output.setReal( value );
		return output;
	}

}
