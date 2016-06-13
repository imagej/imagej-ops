
package net.imagej.ops.learning.neuralnet;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Learning.ConvolutionLayerBackProp.class,
	priority = Priority.HIGH_PRIORITY)
public class DefaultConvolutionLayerBackprop<T extends NativeType<T> & RealType<T>>
	extends
	AbstractBinaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
	implements Ops.Learning.ConvolutionLayerBackProp
{

	@Parameter
	private OpService ops;

	/**
	 * The output type. If null a default output type will be used.
	 */
	@Parameter(required = false)
	private Type<T> outType;

	@Override
	public RandomAccessibleInterval<T> compute2(
		RandomAccessibleInterval<T> input1, RandomAccessibleInterval<T> input2)
	{
		// initialize outType
		if (outType == null) {
			Object temp = new DoubleType();
			outType = (Type<T>) temp;
		}

		// initialize kernel
		RandomAccessibleInterval<T> kernel = ops.create().kernelGauss(1., 2, outType
			.createVariable());
		// initialize activation function
		final Op activationFunction = ops.op("math.tanh", DoubleType.class,
			DoubleType.class);
		// Iterate this
		// Convolve
		RandomAccessibleInterval<T> convolved = ops.filter().convolve(input1,
			kernel);
		// Apply activation function
		RandomAccessibleInterval<T> outActivated = ops.copy().rai(convolved);
		ops.map(Views.iterable(convolved), Views.iterable(outActivated),
			(UnaryComputerOp<T, T>) activationFunction);

		// Calculate error
		RandomAccessibleInterval<T> error = (RandomAccessibleInterval<T>) ops.math()
			.subtract(Views.iterable(input2), Views.iterable(outActivated));

		// Backpropagate error
		// Update kernel
		// Done iterate

		// TODO Auto-generated method stub
		return kernel;
	}

}
