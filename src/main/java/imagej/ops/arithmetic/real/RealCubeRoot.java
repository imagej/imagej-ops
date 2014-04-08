package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.CubeRoot;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the cube root of the real
 * component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = CubeRoot.NAME )
public class RealCubeRoot< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements CubeRoot
{
	@Override
	public O compute( I x, O output )
	{
		output.setReal( Math.cbrt( x.getRealDouble() ) );
		return output;
	}

}
