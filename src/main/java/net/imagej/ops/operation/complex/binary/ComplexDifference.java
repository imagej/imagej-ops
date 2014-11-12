package net.imagej.ops.operation.complex.binary;

import net.imagej.ops.AbstractFunction; 
import net.imagej.ops.Op;
import net.imglib2.type.numeric.NumericType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Difference.NAME, priority = Priority.LOW_PRIORITY)
public class ComplexDifference<T extends NumericType<T>> extends
		AbstractFunction<T, T> implements Difference {

	@Parameter
	T value;

	@Override
	public T compute(T input, T output) {
		output.set(input);
		output.sub(value);
		String s = output.toString();

		String z = Integer.toString(0);

		int value = s.compareTo(z);
		if (value < 0) {
			output.mul(-1);
		}
		return output;
	}

}