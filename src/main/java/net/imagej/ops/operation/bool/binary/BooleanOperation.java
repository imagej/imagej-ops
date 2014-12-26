package net.imagej.ops.operation.bool.binary;

import net.imglib2.ops.operation.BinaryOperation;
import net.imglib2.type.logic.BitType;

public abstract class BooleanOperation implements ComplexBinaryOperation {

	

	@Override
	public BinaryOperation<BitType, BitType, BitType> copy() {
		return null;
	}

}
