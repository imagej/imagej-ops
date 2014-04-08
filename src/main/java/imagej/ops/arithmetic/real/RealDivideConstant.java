package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.DivideConstant;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the division of the real
 * component of an input real number by a constant value. The constant value is
 * specified in the constructor. In the case of division by zero the value is
 * set to a value also specified in the constructor.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = DivideConstant.NAME )
public class RealDivideConstant< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements DivideConstant
{
	private final double constant;

	private final double dbzVal;

	public RealDivideConstant( double constant, double dbzVal )
	{
		this.constant = constant;
		this.dbzVal = dbzVal;
	}

	@Override
	public O compute( I x, O output )
	{
		if ( constant == 0 )
		{
			output.setReal( dbzVal );
		}
		else
		{
			double value = x.getRealDouble() / constant;
			output.setReal( value );
		}
		return output;
	}

}
