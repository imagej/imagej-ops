package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Signum;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the signum of the real
 * component of an input real number. It equals -1 if the input number is less
 * than 0, it equals 1 if the input number is greater than 0, and it equals 0 if
 * the input number equals 0.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = Signum.NAME )
public class RealSignum< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements Signum
{
	@Override
	public O compute( I x, O output )
	{
		output.setReal( Math.signum( x.getRealDouble() ) );
		return output;
	}

}
