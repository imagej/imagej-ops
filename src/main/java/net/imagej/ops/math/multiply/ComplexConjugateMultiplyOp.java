
package net.imagej.ops.math.multiply;

import net.imagej.ops.Contingent;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractBinaryComputerOp;
import net.imglib2.type.numeric.ComplexType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Math.ComplexConjugateMultiply.class,
	priority = Priority.LOW_PRIORITY)
public class ComplexConjugateMultiplyOp<T extends ComplexType<T>> extends
	AbstractBinaryComputerOp<T, T, T> implements
	Ops.Math.ComplexConjugateMultiply, Contingent
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
	public void compute2(final T input1, final T input2, final T output) {
		T temp = input2.copy();
		temp.complexConjugate();
		temp.mul(input1);
		output.set(temp);

	}

}
