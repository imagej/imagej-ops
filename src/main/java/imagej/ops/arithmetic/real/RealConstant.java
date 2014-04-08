package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Constant;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to a constant value. The
 * constant value is specified in the constructor.
 * 
 * @author Barry DeZonia
 */

@Plugin( type = Op.class, name = Constant.NAME )
public class RealConstant< I extends RealType< I >, O extends RealType< O >> extends AbstractFunction< I, O >
		implements Constant
{
	private final double constant;

	public RealConstant( double constant )
	{
		this.constant = constant;
	}

	@Override
	public O compute( I x, O output )
	{
		output.setReal( constant );
		return output;
	}

}
