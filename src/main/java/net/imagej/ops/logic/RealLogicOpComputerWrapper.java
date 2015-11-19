package net.imagej.ops.logic;

import net.imagej.ops.ComputerWrapper;
import net.imagej.ops.Op;
import net.imagej.ops.OpEnvironment;
import net.imglib2.type.numeric.RealType;

public class RealLogicOpComputerWrapper<I extends RealType<I>, O extends RealType<O>> implements ComputerWrapper<I,O> {
	

	@Override
	public void compute(OpEnvironment ops, Class<? extends Op> opClass, I input, O output)
	{
		Boolean result = (Boolean) ops.run(opClass, input, output);
		if(result)
		{
			double maxValue = output.getMaxValue();
			output.setReal(maxValue);
		}
		else
		{
			output.setReal(0.0);
		}	
	}

}
