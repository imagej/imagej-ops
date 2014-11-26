package net.imagej.ops.operation.complex.binary;

import net.imagej.ops.AbstractFunction;
import net.imagej.ops.AbstractInplaceFunction;
import net.imagej.ops.Op;
import net.imglib2.type.numeric.NumericType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Divide.NAME, priority = Priority.LOW_PRIORITY)
public class ComplexDivide<T extends NumericType<T>> extends
AbstractInplaceFunction<T> implements Divide {

	@Parameter
	T value;

	@Override
	public T compute(T arg) {
		arg.div(value);

		return arg;
	}

}