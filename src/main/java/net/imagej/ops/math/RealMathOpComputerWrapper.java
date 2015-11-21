package net.imagej.ops.math;

import net.imagej.ops.ComputerOp;
import net.imagej.ops.ComputerWrapper;
import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;

public class RealMathOpComputerWrapper<I extends RealType<I>, O extends RealType<O>> implements ComputerWrapper<I,O> {
	
	@Parameter
	OpService ops;
	
	ComputerOp<I,O> op = null;
	
	@Override
	public void compute(Class<? extends Op> opClass, I input, O output)
	{
		if(op == null)
		{
			this.op = ops.computer(opClass, output, input);
		}
		this.op.compute(input, output);
	}

}
