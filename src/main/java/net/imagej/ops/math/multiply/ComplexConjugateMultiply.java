
package net.imagej.ops.math.multiply;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.ComplexType;

@Plugin(type = Ops.Math.ComplexConjugateMultiply.class, priority = Priority.LOW_PRIORITY)
public class ComplexConjugateMultiply<T extends ComplexType<T>> extends
	AbstractBinaryComputerOp<IterableInterval<T>, IterableInterval<T>, IterableInterval<T>>
	implements Ops.Math.ComplexConjugateMultiply, Contingent
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

			tmp.set(cursorI2.get());
			tmp.complexConjugate();
			tmp.mul(cursorI1.get());

			cursorO.get().set(tmp);
		}

	}

}
