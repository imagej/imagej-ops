package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.AndConstant;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the logical AND of the
 * real component of an input real number with a constant value. The constant
 * value is specified in the constructor.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = AndConstant.NAME )
public class RealAndConstant< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements AndConstant
{
	private final long constant;

	public RealAndConstant( long constant )
	{
		this.constant = constant;
	}

	@Override
	public O compute( I x, O output )
	{
		long value = constant & ( long ) x.getRealDouble();
		output.setReal( value );
		return output;
	}

}
