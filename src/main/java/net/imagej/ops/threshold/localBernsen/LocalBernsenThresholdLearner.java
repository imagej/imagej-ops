package net.imagej.ops.threshold.localBernsen;

import net.imagej.ops.Op;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.AbstractUnaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractUnaryFunctionOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.threshold.ThresholdLearner;
import net.imagej.ops.threshold.localMidGrey.LocalMidGrey;
import net.imglib2.IterableInterval;
import net.imglib2.type.BooleanType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Local threshold method similar to {@link LocalMidGrey}, but uses a constant
 * value rather than the value of the input pixel when the contrast in the
 * neighborhood of that pixel is too small.
 *
 * @author Jonathan Hale
 * @author Stefan Helfrich (University of Konstanz)
 * @param <I> type of input
 * @param <O> type of output
 */
@Plugin(type = Op.class)
public class LocalBernsenThresholdLearner<I extends RealType<I>, O extends BooleanType<O>>
	extends AbstractUnaryFunctionOp<IterableInterval<I>, UnaryComputerOp<I, O>>
	implements ThresholdLearner<I, O>
{

	@Parameter
	private double constrastThreshold;

	@Parameter
	private double halfMaxValue;
	
	private UnaryFunctionOp<Iterable<I>, Pair<I, I>> minMaxFunc;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public UnaryComputerOp<I, O> compute1(final IterableInterval<I> input) {
		if (minMaxFunc == null) {
			minMaxFunc = (UnaryFunctionOp) Functions.unary(ops(), Ops.Stats.MinMax.class, Pair.class, input);
		}
		
		final Pair<I, I> outputs = minMaxFunc.compute1(input);
		final double minValue = outputs.getA().getRealDouble();
		final double maxValue = outputs.getB().getRealDouble();
		final double midGrey = (maxValue + minValue) / 2.0;

		UnaryComputerOp<I, O> predictorOp = new AbstractUnaryComputerOp<I, O>() {

			@Override
			public void compute1(I in, O out) {
				if ((maxValue - minValue) < constrastThreshold) {
					out.set(midGrey >= halfMaxValue);
				}
				else {
					out.set(in.getRealDouble() >= midGrey);
				}
			}
		};
		predictorOp.setEnvironment(ops());
		predictorOp.initialize();

		return predictorOp;
	}

}
