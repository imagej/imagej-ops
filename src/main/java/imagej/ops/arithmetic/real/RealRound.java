package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Round;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the rounding of the real
 * component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = Round.NAME )
public class RealRound< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements Round
{
	@Override
	public O compute( I x, O output )
	{
		output.setReal( Math.round( x.getRealDouble() ) );
		return output;
	}

}
