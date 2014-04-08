package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.MinConstant;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the real component of an
 * input real number unless it is less then a minimum value. If it is less than
 * the minimum value then it sets the output real component to that minimum
 * value. The minimum value is specified in the constructor.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = MinConstant.NAME )
public class RealMinConstant< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements MinConstant
{
	private final double constant;

	public RealMinConstant( double constant )
	{
		this.constant = constant;
	}

	@Override
	public O compute( I x, O output )
	{
		double value = x.getRealDouble();
		if ( value > constant )
			output.setReal( value );
		else
			output.setReal( constant );
		return output;
	}

}
