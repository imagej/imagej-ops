package net.imagej.ops.operation.complex.binary;

import net.imagej.ops.Op;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

public class ComplexAdd<T> implements ComplexBinaryOperation{
	
	public O compute(I1 z1, I2 z2, O output) {
		double x = z1.getRealDouble() + z2.getRealDouble();
		double y = z1.getImaginaryDouble() + z2.getImaginaryDouble();
		output.setComplexNumber(x, y);
		return output;
	}
	

}
