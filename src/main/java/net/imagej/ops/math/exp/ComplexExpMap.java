
package net.imagej.ops.math.exp;

import net.imagej.ops.Ops;
import net.imagej.ops.map.MapUnaryComputers;
import net.imagej.ops.math.UnaryComplexTypeMath.ComplexExp;
import net.imagej.ops.special.computer.Computers;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imglib2.Dimensions;
import net.imglib2.IterableInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Math.ComplexConjugateMultiply.class,
	priority = Priority.LOW_PRIORITY)
public class ComplexExpMap<T extends ComplexType<T> & NativeType<T>> extends
	AbstractUnaryFunctionOp<IterableInterval<T>, IterableInterval<T>> implements
	Ops.Math.ComplexConjugateMultiply
{

	UnaryComputerOp<T, T> exp;

	private UnaryComputerOp<IterableInterval<T>, IterableInterval<T>> map;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initialize() {
		super.initialize();

		exp = (UnaryComputerOp) Computers.unary(ops(), ComplexExp.class,
			RealType.class, RealType.class);

		map = (UnaryComputerOp) Computers.unary(ops(),
			MapUnaryComputers.IIToIIParallel.class, IterableInterval.class,
			IterableInterval.class, exp);

	}

	@Override
	public IterableInterval<T> compute1(final IterableInterval<T> input1) {

		IterableInterval<T> out = ops().create().img((Dimensions) input1, input1
			.firstElement());
		map.compute1(input1, out);
		return out;
	}

}
