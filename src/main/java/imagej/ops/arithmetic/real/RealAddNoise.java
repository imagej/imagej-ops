package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.AddNoise;

import java.util.Random;

import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the addition of the real
 * component of an input real number with an amount of Gaussian noise. The noise
 * parameters are specified in the constructor.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = AddNoise.NAME )
public class RealAddNoise< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements AddNoise
{
	private final double rangeMin;

	private final double rangeMax;

	private final double rangeStdDev;

	private final Random rng;

	/**
	 * Constructor specifying noise parameters.
	 * 
	 * @param min
	 *            - the desired lower bound on the output pixel values
	 * @param max
	 *            - the desired upper bound on the output pixel values
	 * @param stdDev
	 *            - the stand deviation of the gaussian random variable
	 */
	public RealAddNoise( double min, double max, double stdDev )
	{
		this.rangeMin = min;
		this.rangeMax = max;
		this.rangeStdDev = stdDev;
		this.rng = new Random();
		this.rng.setSeed( System.currentTimeMillis() );
	}

	@Override
	public O compute( I x, O output )
	{
		int i = 0;
		do
		{
			double newVal = x.getRealDouble() + ( rng.nextGaussian() * rangeStdDev );
			if ( ( rangeMin <= newVal ) && ( newVal <= rangeMax ) )
			{
				output.setReal( newVal );
				return output;
			}
			if ( i++ > 100 )
				throw new IllegalArgumentException( "noise function failing to terminate. probably misconfigured." );
		}
		while ( true );
	}

}
