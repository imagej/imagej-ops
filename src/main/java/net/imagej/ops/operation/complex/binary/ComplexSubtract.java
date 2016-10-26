package net.imagej.ops.operation.complex.binary;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.AbstractInplaceFunction;
import net.imagej.ops.Op;
import net.imglib2.type.numeric.NumericType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Average.NAME, priority = Priority.LOW_PRIORITY)
public class ComplexSubtract<T extends NumericType<T>> extends
AbstractInplaceFunction<T> implements Subtract {

	@Parameter
	T value;

	@Override
	public T compute(T arg) {
		arg.sub(value);
		return arg;
	}



}