package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.GammaConstant;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the gamma value of the
 * real component of an input real number. The constant value is specified in
 * the constructor.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = GammaConstant.NAME )
public class RealGammaConstant< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements GammaConstant
{
	private final double constant;

	public RealGammaConstant( double constant )
	{
		this.constant = constant;
	}

	@Override
	public O compute( I x, O output )
	{
		double inputVal = x.getRealDouble();
		if ( inputVal <= 0 )
			output.setReal( 0 );
		else
		{
			double value = Math.exp( this.constant * Math.log( inputVal ) );
			output.setReal( value );
		}
		return output;
	}

}
