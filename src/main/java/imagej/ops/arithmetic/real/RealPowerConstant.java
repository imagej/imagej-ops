package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.PowerConstant;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the raising of the real
 * component of an input real number to a constant value. The constant value is
 * specified in the constructor.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = PowerConstant.NAME )
public class RealPowerConstant< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements PowerConstant
{
	private final double constant;

	public RealPowerConstant( double constant )
	{
		this.constant = constant;
	}

	@Override
	public O compute( I x, O output )
	{
		double value = Math.pow( x.getRealDouble(), constant );
		output.setReal( value );
		return output;
	}

}
