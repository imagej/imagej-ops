
package net.imagej.ops.threshold.manual;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imagej.ops.threshold.ConstantThresholder;
import net.imagej.ops.threshold.GlobalThresholder;
import net.imglib2.IterableInterval;
import net.imglib2.img.Img;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class)
public class Manual<I extends RealType<I>, O extends BooleanType<O>> extends
	AbstractUnaryHybridCF<IterableInterval<I>, Iterable<O>> implements
	GlobalThresholder<I, O>
{

	@Parameter(required = true)
	public I threshold;

	private ConstantThresholder<I, O> thresholder;

	/** Op that is used for creating the output image */
	protected UnaryFunctionOp<IterableInterval<I>, Img<BitType>> imgCreator;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		imgCreator = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class,
			Img.class, in(), new BitType());

		thresholder = new ConstantThresholder<>(threshold);
	}

	@Override
	public void compute1(final IterableInterval<I> input,
		final Iterable<O> output)
	{
		// FIXME we want to initialize this op and reset the predictor according
		// to the input
		ops().map(output, input, thresholder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<O> createOutput(IterableInterval<I> input) {
		return (Iterable<O>) imgCreator.compute1(input);
	}

}
