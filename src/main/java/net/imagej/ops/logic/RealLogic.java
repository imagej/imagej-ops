package net.imagej.ops.logic;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.Ops;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Basic class for performing logic operations using real values.
 * @author jaywarrick
 *
 */

public class RealLogic {

	private RealLogic()
	{
		// Prevent instantiation of utility class.
	}


	/** Op that computes the Equal logic operation of two RealType values.
	 * True if boolean(A) == boolean(B). Similar to And but their boolean
	 * versions of themselves must be both the same instead of both
	 * just not 0.
	 */
	@Plugin(type = Ops.Logic.Equal.class, priority = 0.1)
	public static class Equal<A extends RealType<A>,B extends RealType<B>> extends AbstractComputerOp<A,B>
	implements Ops.Logic.Equal
	{
		@Override
		public void compute(A input, B output) {
			if((input.getRealDouble() != 0.0 && output.getRealDouble() != 0.0) || (input.getRealDouble() == 0.0 && output.getRealDouble() == 0.0))
			{
				output.setReal(output.getMaxValue());
			}
			else
			{
				output.setReal(0.0);
			}
		}
	}

	/** Op that computes the XOr of two RealType values.
	 * True if A and B are != 0.
	 */
	@Plugin(type = Ops.Logic.And.class, priority = 0.1)
	public static class And<A extends RealType<A>,B extends RealType<B>> extends AbstractComputerOp<A,B>
	implements Ops.Logic.And
	{
		@Override
		public void compute(A input, B output) {
			if(input.getRealDouble() != 0 && output.getRealDouble() != 0)
			{
				output.setReal(output.getMaxValue());
			}
			else
			{
				output.setReal(0.0);
			}
		}
	}

	/** Op that computes the Or of two RealType values.
	 * True if A or B are != 0. 
	 */
	@Plugin(type = Ops.Logic.Or.class, priority = 0.1)
	public static class Or<A extends RealType<A>,B extends RealType<B>> extends AbstractComputerOp<A,B>
	implements Ops.Logic.Or
	{
		@Override
		public void compute(A input, B output) {
			if(input.getRealDouble() != 0.0 || output.getRealDouble() != 0.0)
			{
				output.setReal(output.getMaxValue());;
			}
			else
			{
				output.setReal(0.0);
			}
		}
	}

	/** Op that computes the XOr of two RealType values.
	 * True if A or B are != 0 but not both. 
	 */
	@Plugin(type = Ops.Logic.Xor.class, priority = 0.1)
	public static class XOr<A extends RealType<A>,B extends RealType<B>> extends AbstractComputerOp<A,B>
	implements Ops.Logic.Xor
	{
		@Override
		public void compute(A input, B output) {
			if(input.getRealDouble() != 0.0 || output.getRealDouble() != 0.0)
			{

				if(!(input.getRealDouble() != 0.0 && output.getRealDouble() != 0.0))
				{
					output.setReal(output.getMaxValue());
				}
			}
			output.setReal(0.0);
		}
	}

	/** Op that computes the GreaterThan of two RealType values.
	 * True if A > output. 
	 */
	@Plugin(type = Ops.Logic.GreaterThan.class, priority = 0.1)
	public static class GreaterThan<A extends RealType<A>,B extends RealType<B>> extends AbstractComputerOp<A,B>
	implements Ops.Logic.GreaterThan
	{
		@Override
		public void compute(A input, B output) {
			if(input.getRealDouble() > output.getRealDouble())
			{
				output.setReal(output.getMaxValue());
			}
			else
			{
				output.setReal(0.0);
			}
		}
	}

	/** Op that computes the LessThan of two RealType values.
	 * True if A < output. 
	 */
	@Plugin(type = Ops.Logic.LessThan.class, priority = 0.1)
	public static class LessThan<A extends RealType<A>,B extends RealType<B>> extends AbstractComputerOp<A,B>
	implements Ops.Logic.LessThan
	{
		@Override
		public void compute(A input, B output) {
			if(input.getRealDouble() < output.getRealDouble())
			{
				output.setReal(output.getMaxValue());
			}
			else
			{
				output.setReal(0.0);
			}
		}
	}
}
