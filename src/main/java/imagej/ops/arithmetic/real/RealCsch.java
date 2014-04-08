package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Csch;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the hyperbolic cosecant
 * of the real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = Csch.NAME )
public class RealCsch< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements Csch
{
	@Override
	public O compute( I x, O output )
	{
		double value = 1.0 / Math.sinh( x.getRealDouble() );
		output.setReal( value );
		return output;
	}

}
