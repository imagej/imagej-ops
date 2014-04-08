
package imagej.ops.arithmetic.real;

import imagej.ops.AbstractFunction;
import imagej.ops.Op;
import imagej.ops.arithmetic.Arccsc;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Plugin;

/**
 * Sets the real component of an output real number to the inverse cosecant of
 * the real component of an input real number.
 * 
 * @author Barry DeZonia
 */

@Plugin(type = Op.class, name = Arccsc.NAME)
public class RealArccsc<I extends RealType<I>, O extends RealType<O>> extends
	AbstractFunction<I, O> implements Arccsc
{

	private static final RealArccos<DoubleType, DoubleType> acos =
		new RealArccos<DoubleType, DoubleType>();

	private DoubleType angle = new DoubleType();

	private DoubleType tmp = new DoubleType();

	@Override
	public O compute(I x, O output) {
		double xt = x.getRealDouble();
		if ((xt > -1) && (xt < 1)) throw new IllegalArgumentException(
			"arccsc(x) : x out of range");
		else if (xt == -1) output.setReal(-Math.PI / 2);
		else if (xt == 1) output.setReal(Math.PI / 2);
		else {
			tmp.setReal(Math.sqrt(xt * xt - 1) / xt);
			acos.compute(tmp, angle);
			output.setReal(angle.getRealDouble());
		}
		return output;
	}

}
