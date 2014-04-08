package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Coth;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the hyperbolic cotangent
 * of the real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = Coth.NAME )
public class RealCoth< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements Coth
{
	@Override
	public O compute( I x, O output )
	{
		double value = 1.0 / Math.tanh( x.getRealDouble() );
		output.setReal( value );
		return output;
	}

}
