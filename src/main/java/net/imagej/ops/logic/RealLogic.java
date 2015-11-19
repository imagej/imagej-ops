package net.imagej.ops.logic;

import net.imagej.ops.AbstractOp;
import net.imagej.ops.Ops;
import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

public class RealLogic {

	private RealLogic()
	{
		// Prevent instantiation of utility class.
	}

	
	/** Op that computes the Equal logic operation of two RealType values.
	 * True if A == B. Similar to And but must be both the same
	 * instead of just not 0.
	 */
	@Plugin(type = Ops.Logic.Equal.class, priority = 0.1)
	public static class Equal<A extends RealType<A>,B extends RealType<B>> extends AbstractOp
	implements Ops.Logic.Equal
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter
		private A a;

		@Parameter
		private B b;

		@Override
		public void run() {
			if((a.getRealDouble() != 0.0 && b.getRealDouble() != 0.0) || (a.getRealDouble() == 0.0 && b.getRealDouble() == 0.0))
			{
				result = true;
			}
		}
	}
	
	/** Op that computes the XOr of two RealType values.
	 * True if A and B are != 0. Could have also done A and B are != 0.
	 */
	@Plugin(type = Ops.Logic.And.class, priority = 0.1)
	public static class And<A extends RealType<A>,B extends RealType<B>> extends AbstractOp
	implements Ops.Logic.And
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter(required=true, persist=false)
		private A a;

		@Parameter(required=true, persist=false)
		private B b;

		@Override
		public void run() {
			if(a.getRealDouble() != 0 && b.getRealDouble() != 0)
			{
				result = true;
			}
		}
	}

	/** Op that computes the Or of two RealType values.
	 * True if A or B are != 0. 
	 */
	@Plugin(type = Ops.Logic.Or.class, priority = 0.1)
	public static class Or<A extends RealType<A>,B extends RealType<B>> extends AbstractOp
	implements Ops.Logic.Or
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter(required=true, persist=false)
		private A a;

		@Parameter(required=true, persist=false)
		private B b;
		
		@Override
		public void run() {
			if(a.getRealDouble() != 0.0 || b.getRealDouble() != 0.0)
			{
				result = true;
			}
		}
	}

	/** Op that computes the XOr of two RealType values.
	 * True if A or B are != 0 but not both. 
	 */
	@Plugin(type = Ops.Logic.Xor.class, priority = 0.1)
	public static class XOr<A extends RealType<A>,B extends RealType<B>> extends AbstractOp
	implements Ops.Logic.Xor
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter(required=true, persist=false)
		private A a;

		@Parameter(required=true, persist=false)
		private B b;

		@Override
		public void run() {
			if(a.getRealDouble() != 0.0 || b.getRealDouble() != 0.0)
			{
				
				if(!(a.getRealDouble() != 0.0 && b.getRealDouble() != 0.0))
				{
					result = true;
					return;
				}
			}
			result = false;
		}
	}
	
	/** Op that computes the GreaterThan of two RealType values.
	 * True if A > B. 
	 */
	@Plugin(type = Ops.Logic.GreaterThan.class, priority = 0.1)
	public static class GreaterThan<A extends RealType<A>,B extends RealType<B>> extends AbstractOp
	implements Ops.Logic.GreaterThan
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter(required=true, persist=false)
		private A a;

		@Parameter(required=true, persist=false)
		private B b;

		@Override
		public void run() {
			result = a.getRealDouble() > b.getRealDouble();
		}
	}
	
	/** Op that computes the LessThan of two RealType values.
	 * True if A < B. 
	 */
	@Plugin(type = Ops.Logic.LessThan.class, priority = 0.1)
	public static class LessThan<A extends RealType<A>,B extends RealType<B>> extends AbstractOp
	implements Ops.Logic.LessThan
	{
		@Parameter(type = ItemIO.OUTPUT)
		private boolean result;

		@Parameter(required=true, persist=false)
		private A a;

		@Parameter(required=true, persist=false)
		private B b;

		@Override
		public void run() {
			result = a.getRealDouble() < b.getRealDouble();
		}
	}
}
