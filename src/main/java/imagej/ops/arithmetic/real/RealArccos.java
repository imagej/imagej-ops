package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Arccos;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the inverse cosine of the
 * real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = Arccos.NAME )
public class RealArccos< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements Arccos
{
	@Override
	public O compute( I x, O output )
	{
		double value = Math.acos( x.getRealDouble() );
		output.setReal( value );
		return output;
	}

}
