package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Invert;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the inversion of the real
 * component of an input real number about a range. The range is specified in
 * the constructor.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = Invert.NAME )
public class RealInvert< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements Invert
{
	private double specifiedMin;

	private double specifiedMax;

	/**
	 * Constructor.
	 * 
	 * @param specifiedMin
	 *            - minimum value of the range to invert about
	 * @param specifiedMax
	 *            - maximum value of the range to invert about
	 */
	public RealInvert( final double specifiedMin, final double specifiedMax )
	{
		this.specifiedMax = specifiedMax;
		this.specifiedMin = specifiedMin;
	}

	@Override
	public O compute( I x, O output )
	{
		double value = specifiedMax - ( x.getRealDouble() - specifiedMin );
		output.setReal( value );
		return output;
	}

}
