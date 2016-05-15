
package net.imagej.ops.math;

import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imglib2.type.numeric.ComplexType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

public class UnaryComplexTypeMath {

	/**
	 * Computes exp(a+bj) using Euler's formula
	 */
	@Plugin(type = Ops.Math.Exp.class, priority = Priority.NORMAL_PRIORITY - 1)
	public static class ComplexExp<I extends ComplexType<I>, O extends ComplexType<O>>
		extends AbstractUnaryComputerOp<I, O> implements Ops.Math.Exp
	{

		@Override
		public void compute1(final I input, final O output) {
			output.setReal(Math.exp(input.getRealDouble()) * Math.cos(input
				.getImaginaryDouble()));
			output.setImaginary(Math.exp(input.getRealDouble()) * Math.sin(input
				.getImaginaryDouble()));
		}
	}

}
