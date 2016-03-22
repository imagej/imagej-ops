
package net.imagej.ops.math.multiply;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.map.MapBinaryComputers;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Math.ComplexConjugateMultiply.class,
	priority = Priority.LOW_PRIORITY)
public class ComplexConjugateMultiplyMap<T extends ComplexType<T>> extends
	AbstractBinaryComputerOp<IterableInterval<T>, IterableInterval<T>, IterableInterval<T>>
	implements Ops.Math.ComplexConjugateMultiply, Contingent
{

	BinaryComputerOp<T, T, T> multiply;

	private BinaryComputerOp<IterableInterval<T>, IterableInterval<T>, IterableInterval<T>> map;

	@Override
	@SuppressWarnings("unchecked")
	public void initialize() {
		super.initialize();

		multiply = (BinaryComputerOp) Computers.binary(ops(),
			ComplexConjugateMultiplyOp.class, RealType.class, RealType.class,
			RealType.class);

		map = (BinaryComputerOp) Computers.binary(ops(),
			MapBinaryComputers.IIAndIIToIIParallel.class, IterableInterval.class,
			IterableInterval.class, IterableInterval.class, multiply);

	}

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
		map.compute2(input1, input2, output);

	}

}
