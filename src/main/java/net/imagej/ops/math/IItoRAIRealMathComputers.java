package net.imagej.ops.math;

import net.imagej.ops.ComputerWrapper;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imglib2.type.numeric.RealType;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

public class IItoRAIRealMathComputers {

	@Plugin(type = Ops.Math.Add.class, priority = Priority.HIGH_PRIORITY + 0.1)
	public static class Add<I extends RealType<I>,O extends RealType<O>> extends AbstractIItoRAIRealWrappedComputerOp<I,O>
		implements Ops.Math.Add
	{
		@Override
		protected Class<? extends Op> getWorkerClass()
		{
			return Ops.Math.Add.class;
		}
		@Override
		protected ComputerWrapper<I,O> createWrapper()
		{
			return new RealMathOpComputerWrapper<I,O>();
		}
	}
	
	@Plugin(type = Ops.Math.Subtract.class, priority = Priority.HIGH_PRIORITY + 0.1)
	public static class Subtract<I extends RealType<I>,O extends RealType<O>> extends AbstractIItoRAIRealWrappedComputerOp<I,O>
		implements Ops.Math.Subtract
	{
		@Override
		protected Class<? extends Op> getWorkerClass()
		{
			return Ops.Math.Subtract.class;
		}
		@Override
		protected ComputerWrapper<I,O> createWrapper()
		{
			return new RealMathOpComputerWrapper<I,O>();
		}
	}
	
	@Plugin(type = Ops.Math.Multiply.class, priority = Priority.HIGH_PRIORITY + 0.1)
	public static class Multiply<I extends RealType<I>,O extends RealType<O>> extends AbstractIItoRAIRealWrappedComputerOp<I,O>
		implements Ops.Math.Multiply
	{
		@Override
		protected Class<? extends Op> getWorkerClass()
		{
			return Ops.Math.Multiply.class;
		}
		@Override
		protected ComputerWrapper<I,O> createWrapper()
		{
			return new RealMathOpComputerWrapper<I,O>();
		}
	}
	
	@Plugin(type = Ops.Math.Divide.class, priority = Priority.HIGH_PRIORITY + 0.1)
	public static class Divide<I extends RealType<I>,O extends RealType<O>> extends AbstractIItoRAIRealWrappedComputerOp<I,O>
		implements Ops.Math.Divide
	{
		@Override
		protected Class<? extends Op> getWorkerClass()
		{
			return Ops.Math.Divide.class;
		}
		@Override
		protected ComputerWrapper<I,O> createWrapper()
		{
			return new RealMathOpComputerWrapper<I,O>();
		}
	}
}
