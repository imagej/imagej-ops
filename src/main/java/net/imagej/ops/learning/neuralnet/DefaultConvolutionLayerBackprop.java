
package net.imagej.ops.learning.neuralnet;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
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
		
		int numDimensions = input1.numDimensions();

		// initialize kernel
		RandomAccessibleInterval<T> kernel = ops.create().kernelGauss(1., 2, outType
			.createVariable());
		// initialize activation function
		final Op activationFunction = ops.op("math.tanh", DoubleType.class,
			DoubleType.class);
		// define the valid interval
		final long[] validMin = new long[numDimensions];
		final long[] validMax = new long[numDimensions];
		for( int d = 0; d < input1.numDimensions(); d++ ) {
			validMin[d] = (long) Math.floor( kernel.dimension(d) / 2 );
			validMax[d] = (long) ( input1.dimension(d) - Math.floor( kernel.dimension(d) / 2 ) );
			System.out.println( "Valid [d=" + d + "] min = " + validMin[d] + " max = " + validMax[d] );
		}		
		// define padded interval
		final long[] paddedSize = new long[numDimensions];
		for (int d = 0; d < numDimensions; ++d) {
			paddedSize[d] = (int) input1.dimension(d) + (int) kernel.dimension(d) - 1;
		}
		// define valid target
		RandomAccessibleInterval<T> targetValid = Views.zeroMin( Views.interval(input2, validMin, validMax) );
		
		// Iterate this
		// Convolve
		RandomAccessibleInterval<T> convolved = ops.filter().convolve(input1,
			kernel);
		// Truncate to valid convolution output
		RandomAccessibleInterval<T> convolvedValid = Views.zeroMin( Views.interval(convolved, validMin, validMax) );
		
		// Apply activation function
		RandomAccessibleInterval<T> outActivated = ops.copy().rai(convolvedValid);
		
		
		ops.map(Views.iterable(convolvedValid), Views.iterable(outActivated),
			(UnaryComputerOp<T, T>) activationFunction);

		// Calculate error
		RandomAccessibleInterval<T> error = (RandomAccessibleInterval<T>) ops.math()
			.subtract(Views.iterable(targetValid), Views.iterable(outActivated));

		// Backpropagate error (doublecheck logic)
		// Flip kernel (this isn't N-D, it is 2D, ewww)
		RandomAccessibleInterval<T> flippedKernel = Views.invertAxis( Views.invertAxis(kernel, 0), 1 );		
		
		Img<T> errorProp = ops.create().img( input1 );
		
		RandomAccessibleInterval<T> backpropConvolution = ops.filter().convolve( errorProp, Views.extendZero(error),
				flippedKernel );

		// Update kernel
		// Done iterate

		// TODO Auto-generated method stub
		return errorProp;
	}

}
