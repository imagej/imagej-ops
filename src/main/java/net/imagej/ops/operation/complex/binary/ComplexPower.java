package net.imagej.ops.operation.complex.binary;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.Op;
import net.imglib2.type.numeric.NumericType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Exponent.NAME, priority = Priority.LOW_PRIORITY)
public class ComplexPower<T extends NumericType<T>> extends
		AbstractFunction<T, T> implements Exponent {

	@Parameter
	private int value;

	@Override
	public T compute(T input, T output) {
		output.set(input);

		T mult = input.copy();

		for (int i = 0; i < value - 1; i++) {
			output.mul(mult);
		}

		return output;
	}

}