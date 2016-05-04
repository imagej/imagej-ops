
package net.imagej.ops.filter.addPoissonNoise;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.map.MapUnaryComputers;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Filter.AddPoissonNoise.class,
	priority = Priority.LOW_PRIORITY)
public class AddPoissonNoiseMap<T extends ComplexType<T>> extends
	AbstractUnaryComputerOp<IterableInterval<T>, IterableInterval<T>> implements
	Ops.Filter.AddPoissonNoise, Contingent
{

	UnaryComputerOp<T, T> noiser;

	private UnaryComputerOp<IterableInterval<T>, IterableInterval<T>> map;

	@Override
	@SuppressWarnings("unchecked")
	public void initialize() {
		super.initialize();

		noiser = (UnaryComputerOp) Computers.unary(ops(),
			AddPoissonNoiseRealType.class, RealType.class, RealType.class);

		map = (UnaryComputerOp) Computers.unary(ops(),
			MapUnaryComputers.IIToIIParallel.class, IterableInterval.class,
			IterableInterval.class, noiser);

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
	public void compute1(final IterableInterval<T> input,
		final IterableInterval<T> output)
	{
		map.compute1(input, output);
	}

}
