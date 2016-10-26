package net.imagej.ops.operation.complex.binary;

import net.imagej.ops.AbstractFunction; 
import net.imagej.ops.AbstractInplaceFunction;
import net.imagej.ops.Op;
import net.imglib2.type.numeric.NumericType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = Difference.NAME, priority = Priority.LOW_PRIORITY)
public class ComplexDifference<T extends NumericType<T>> extends
AbstractInplaceFunction<T> implements Difference {

	@Parameter
	T value;

	@Override
	public T compute(T arg) {
		arg.sub(value);
		String s = arg.toString();

		String z = Integer.toString(0);

		int value = s.compareTo(z);
		if (value < 0) {
			arg.mul(-1);
		}
		return arg;
	}

	}

