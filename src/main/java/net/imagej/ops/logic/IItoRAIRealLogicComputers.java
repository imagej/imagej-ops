package net.imagej.ops.logic;

import net.imagej.ops.ComputerWrapper;
import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.math.AbstractIItoRAIRealWrappedComputerOp;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

public class IItoRAIRealLogicComputers {

	@Plugin(type = Ops.Logic.Equal.class, priority = 0.1)
	public static class Equal<I extends RealType<I>,O extends RealType<O>> extends AbstractIItoRAIRealWrappedComputerOp<I,O>
		implements Ops.Logic.Equal
	{
		@Override
		protected Class<? extends Op> getWorkerClass()
		{
			return Ops.Logic.Equal.class;
		}
		@Override
		protected ComputerWrapper<I,O> createWrapper()
		{
			return new RealLogicOpComputerWrapper<I,O>();
		}
	}
	
	@Plugin(type = Ops.Logic.And.class, priority = 0.1)
	public static class And<I extends RealType<I>,O extends RealType<O>> extends AbstractIItoRAIRealWrappedComputerOp<I,O>
		implements Ops.Logic.And
	{
		@Override
		protected Class<? extends Op> getWorkerClass()
		{
			return Ops.Logic.And.class;
		}
		@Override
		protected ComputerWrapper<I,O> createWrapper()
		{
			return new RealLogicOpComputerWrapper<I,O>();
		}
	}
	
	@Plugin(type = Ops.Logic.Or.class, priority = 0.1)
	public static class Or<I extends RealType<I>,O extends RealType<O>> extends AbstractIItoRAIRealWrappedComputerOp<I,O>
		implements Ops.Logic.Or
	{
		@Override
		protected Class<? extends Op> getWorkerClass()
		{
			return Ops.Logic.Or.class;
		}
		@Override
		protected ComputerWrapper<I,O> createWrapper()
		{
			return new RealLogicOpComputerWrapper<I,O>();
		}
	}
	
	@Plugin(type = Ops.Logic.Xor.class, priority = 0.1)
	public static class Xor<I extends RealType<I>,O extends RealType<O>> extends AbstractIItoRAIRealWrappedComputerOp<I,O>
		implements Ops.Logic.Xor
	{
		@Override
		protected Class<? extends Op> getWorkerClass()
		{
			return Ops.Logic.Xor.class;
		}
		@Override
		protected ComputerWrapper<I,O> createWrapper()
		{
			return new RealLogicOpComputerWrapper<I,O>();
		}
	}
	
	@Plugin(type = Ops.Logic.GreaterThan.class, priority = 0.1)
	public static class GreaterThan<I extends RealType<I>,O extends RealType<O>> extends AbstractIItoRAIRealWrappedComputerOp<I,O>
		implements Ops.Logic.GreaterThan
	{
		@Override
		protected Class<? extends Op> getWorkerClass()
		{
			return Ops.Logic.GreaterThan.class;
		}
		@Override
		protected ComputerWrapper<I,O> createWrapper()
		{
			return new RealLogicOpComputerWrapper<I,O>();
		}
	}
	
	@Plugin(type = Ops.Logic.LessThan.class, priority = 0.1)
	public static class LessThan<I extends RealType<I>,O extends RealType<O>> extends AbstractIItoRAIRealWrappedComputerOp<I,O>
		implements Ops.Logic.LessThan
	{
		@Override
		protected Class<? extends Op> getWorkerClass()
		{
			return Ops.Logic.LessThan.class;
		}
		@Override
		protected ComputerWrapper<I,O> createWrapper()
		{
			return new RealLogicOpComputerWrapper<I,O>();
		}
	}
}
