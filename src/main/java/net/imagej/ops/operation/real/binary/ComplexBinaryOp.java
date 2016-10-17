package net.imagej.ops.operation.real.binary;

import net.imglib2.ops.operation.real.binary.RealBinaryOperation; 
import net.imglib2.type.numeric.RealType;

public interface ComplexBinaryOp < I1 extends RealType<I1>,
I2 extends RealType<I2>,
O extends RealType<O>>
extends RealBinaryOperation<I1, I2, O> {
	
}
