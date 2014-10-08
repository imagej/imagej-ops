package net.imagej.ops.operation.complex.binary;

public class ComplexAvg {
	public O compute(I1 z1, I2 z2, O output) {
		double x = (z1.getRealDouble() + z2.getRealDouble()) / 2;
		double y = (z1.getImaginaryDouble() + z2.getImaginaryDouble()) / 2;
		output.setComplexNumber(x, y);
		return output;
	}
}
