package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Arcsec;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the inverse secant of the
 * real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = Arcsec.NAME )
public class RealArcsec< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements Arcsec
{
	private final RealArcsin< DoubleType, DoubleType > asin = new RealArcsin< DoubleType, DoubleType >();

	private DoubleType angle = new DoubleType();

	private DoubleType tmp = new DoubleType();

	@Override
	public O compute( I x, O output )
	{
		double xt = x.getRealDouble();
		if ( ( xt > -1 ) && ( xt < 1 ) )
			throw new IllegalArgumentException( "arcsec(x) : x out of range" );
		else if ( xt == -1 )
			output.setReal( Math.PI );
		else if ( xt == 1 )
			output.setReal( 0 );
		else
		{
			tmp.setReal( Math.sqrt( xt * xt - 1 ) / xt );
			asin.compute( tmp, angle );
			double value = angle.getRealDouble();
			if ( xt < -1 )
				value += Math.PI;
			output.setReal( value );
		}
		return output;
	}

}
