
package net.imagej.ops.math.divide;

import net.imagej.ops.AbstractHybridOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Math.Divide.class, priority = Priority.LOW_PRIORITY)
public class DivideHandleZero<T extends RealType<T>> extends
	AbstractHybridOp<IterableInterval<T>, IterableInterval<T>> implements
	Ops.Math.Divide, Contingent
{

	private boolean inPlace;

	// TODO: extend common abstract base class which implements Contingent
	// for dimensionality checking.
	// TODO: code generate this and all add ops to generalize them to other
	// operators.

	@Parameter
	private IterableInterval<T> ii;

	@Override
	public boolean conforms() {
		if (!Intervals.equalDimensions(in(), ii)) return false;
		if (out() == null) return true;
		return Intervals.equalDimensions(ii, out());
	}

	@Override
	public IterableInterval<T> createOutput(final IterableInterval<T> input) {
		inPlace = true;
		return ops().create().img(input, input.firstElement(), null);
	}

	@Override
	public void compute(final IterableInterval<T> input,
		final IterableInterval<T> output)
	{
		final Cursor<T> cursor = ii.cursor();
		final Cursor<T> cursorI = input.cursor();
		final Cursor<T> cursorO = output.cursor();

		final T tmp = input.firstElement().copy();

		while (cursor.hasNext()) {
			cursor.fwd();
			cursorI.fwd();

			if (!inPlace) cursorO.fwd();

			if (cursor.get().getRealFloat() > 0) {
				tmp.set(cursorI.get());
				tmp.div(cursor.get());
				cursorO.get().set(tmp);
			}
			else {
				cursorO.get().setReal(0.0);
			}
		}

	}

}
