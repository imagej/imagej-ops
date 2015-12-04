
package net.imagej.ops.conditional;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractComputerOp;
import net.imagej.ops.ComputerOp;
import net.imagej.ops.Ops;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BoolType;

/**
 * This class works as a conditional operator. If the input is evaluated as true
 * in the given condition Op, it sets the output with the true value; otherwise
 * it sets the output with the false value, or does nothing if the false value
 * is not provided.
 *
 * @author Leon Yang
 * @param <O>
 */
@Plugin(type = Ops.If.class)
public class IfCondition<I, O extends Type<O>> extends AbstractComputerOp<I, O>
	implements Ops.If
{

	@Parameter
	private ComputerOp<I, BoolType> condition;

	@Parameter
	private O trueValue;

	@Parameter(required = false)
	private O falseValue;

	@Override
	public void compute(final I input, final O output) {
		final BoolType truth = new BoolType();
		condition.compute(input, truth);
		if (truth.get()) output.set(trueValue);
		else if (falseValue != null) output.set(falseValue);
	}
}
