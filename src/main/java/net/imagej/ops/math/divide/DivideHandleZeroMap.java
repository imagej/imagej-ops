
package net.imagej.ops.math.divide;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.map.MapBinaryInplace1s;
import net.imagej.ops.special.inplace.AbstractBinaryInplace1Op;
import net.imagej.ops.special.inplace.BinaryInplace1Op;
import net.imagej.ops.special.inplace.Inplaces;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Math.Divide.class, priority = Priority.NORMAL_PRIORITY)
public class DivideHandleZeroMap<T extends RealType<T>> extends
	AbstractBinaryInplace1Op<IterableInterval<T>, IterableInterval<T>> implements
	Ops.Math.Divide, Contingent
{

	private BinaryInplace1Op<T, T, T> divide;

	private BinaryInplace1Op<IterableInterval<T>, IterableInterval<T>, IterableInterval<T>> map;

	@Override
	@SuppressWarnings("unchecked")
	public void initialize() {
		super.initialize();

		divide = (BinaryInplace1Op) Inplaces.binary1(ops(),
			DivideHandleZeroOp.class, RealType.class, RealType.class);

		map = (BinaryInplace1Op) Inplaces.binary1(ops(),
			MapBinaryInplace1s.IIAndII.class, IterableInterval.class,
			IterableInterval.class, divide);

	}

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
	public void mutate1(final IterableInterval<T> input1,
		final IterableInterval<T> input2)
	{
		map.mutate1(input1, input2);
	}

}
