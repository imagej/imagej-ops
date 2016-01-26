
package net.imagej.ops.math.divide;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

@Plugin(type = Ops.Math.Divide.class, priority = Priority.LOW_PRIORITY)
public class DivideHandleZero<T extends RealType<T>> extends
	AbstractBinaryComputerOp<IterableInterval<T>, IterableInterval<T>, IterableInterval<T>>
	implements Ops.Math.Divide, Contingent
{

	// TODO: extend common abstract base class which implements Contingent
	// for dimensionality checking.
	// TODO: code generate this and all add ops to generalize them to other
	// operators.

	@Override
	public boolean conforms() {
		return true;
		/*	if (!Intervals.equalDimensions(in1(), in2())) return false;
			if (!in1().iterationOrder().equals(in2().iterationOrder())) return false;
			if (out() == null) return true;
			return Intervals.equalDimensions(in1(), out()) && in1().iterationOrder()
				.equals(out().iterationOrder());*/
	}

	@Override
	public void compute2(final IterableInterval<T> input1,
		final IterableInterval<T> input2, final IterableInterval<T> output)
	{
		final Cursor<T> cursorI1 = input1.cursor();
		final Cursor<T> cursorI2 = input2.cursor();
		final Cursor<T> cursorO = output.cursor();

		final T tmp = input1.firstElement().copy();

		while (cursorI1.hasNext()) {
			cursorI1.fwd();
			cursorI2.fwd();
			cursorO.fwd();

			// if (!inPlace) cursorO.fwd();

			if (cursorI2.get().getRealFloat() > 0) {
				tmp.set(cursorI1.get());
				tmp.div(cursorI2.get());
				cursorO.get().set(tmp);
			}
			else {
				cursorO.get().setReal(0.0);
			}
		}

	}

}
