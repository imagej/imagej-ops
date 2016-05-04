
package net.imagej.ops.math.divide;

import net.imagej.ops.Ops;
import net.imagej.ops.special.inplace.AbstractBinaryInplace1Op;
import net.imglib2.type.numeric.NumericType;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Math.Divide.class)
public class DivideHandleZeroOp<I extends RealType<I> & NumericType<I>, O extends RealType<O> & NumericType<O>>
	extends AbstractBinaryInplace1Op<I, I> implements Ops.Math.Divide
{

	@Override
	public void mutate1(final I input, final I outin) {
		final I tmp = outin.copy();

		if (outin.getRealFloat() > 0) {

			tmp.set(outin);
			tmp.div(input);
			outin.set(tmp);
		}
		else {
			outin.setReal(0.0);
		}
	}
}
