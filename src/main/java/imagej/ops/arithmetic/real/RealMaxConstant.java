package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.MaxConstant;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the real component of an
 * input real number unless it exceeds a maximum value. If it exceeds the
 * maximum value then it sets the output real component to that maximum value.
 * The maximum value is specified in the constructor.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = MaxConstant.NAME )
public class RealMaxConstant< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements MaxConstant
{
	private final double constant;

	public RealMaxConstant( double constant )
	{
		this.constant = constant;
	}

	@Override
	public O compute( I x, O output )
	{
		double value = x.getRealDouble();
		if ( value < constant )
			output.setReal( value );
		else
			output.setReal( constant );
		return output;
	}

}
