package net.imagej.ops.operation.complex.binary;

import net.imagej.ops.AbstractFunction; 
import net.imagej.ops.Op;
import net.imglib2.type.numeric.NumericType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Average.NAME, priority = Priority.LOW_PRIORITY)
public class ComplexAverage<T extends NumericType<T>> extends
		AbstractFunction<T, T> implements Average {

	@Parameter
	T value;

	@Override
	public T compute(T input, T output) {
		output.set(input);
		output.add(value);
		output.mul(.5);
		return output;
	}

}
