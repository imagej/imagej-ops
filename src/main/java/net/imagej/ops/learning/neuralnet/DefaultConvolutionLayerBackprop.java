
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
public class DefaultConvolutionLayerBackprop<I1 extends NativeType<I1> & RealType<I1>, I2 extends NativeType<I2> & RealType<I2>, O extends NativeType<O> & RealType<O>>
	extends
	AbstractBinaryFunctionOp<RandomAccessibleInterval<I1>, RandomAccessibleInterval<I2>, RandomAccessibleInterval<O>>
	implements Ops.Learning.ConvolutionLayerBackProp
{

	@Parameter
	private OpService ops;

	/**
	 * The output type. If null a default output type will be used.
	 */
	@Parameter(required = false)
	private Type<O> outType;

	@Override
	public RandomAccessibleInterval<O> compute2(
		RandomAccessibleInterval<I1> input1, RandomAccessibleInterval<I2> input2)
	{
		// initialize outType
		if (outType == null) {
			Object temp = new DoubleType();
			outType = (Type<O>) temp;
		}

		
		
		// initialize kernel
		RandomAccessibleInterval<O> kernel = ops.create().kernelGauss(1., 2, outType
			.createVariable());
		// initialize activation function
		final Op activationFunction = ops.op("math.tanh", DoubleType.class, DoubleType.class  );				
		// Iterate this
		// Convolve
		RandomAccessibleInterval<O> convolved = ops.filter().convolve( input1, kernel );
		// Apply activation function
		RandomAccessibleInterval<O> outActivated = ops.copy().rai( convolved );
		ops.map( Views.iterable(convolved), Views.iterable(outActivated), (UnaryComputerOp<O,O>) activationFunction );
		
		// Calculate error
		RandomAccessibleInterval<O> error = ops.math().subtract( (IterableInterval<I2>) input2, (IterableInterval<O>) outActivated );
		// Backpropagate error
		// Update kernel
		// Done iterate

		// TODO Auto-generated method stub
		return kernel;
	}

}
