package net.imagej.ops.math;

import net.imagej.ops.ComputerWrapper;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imglib2.type.numeric.RealType;

public class RealMathOpComputerWrapper<I extends RealType<I>, O extends RealType<O>> implements ComputerWrapper<I,O> {
	
	@Override
	public void compute(OpEnvironment ops, Class<? extends Op> opClass, I input, O output)
	{
		double result = ((RealType<?>) ops.run(opClass, input, output)).getRealDouble();
		output.setReal(result);
	}

}
