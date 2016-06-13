
package net.imagej.ops.learning.neuralnet;

import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ComplexType;

public class ConvolutionLayerBackprop<I1 extends NativeType<I1> & ComplexType<I1>, I2 extends NativeType<I2> & ComplexType<I2>, O extends NativeType<O> & ComplexType<O>>
	extends
	AbstractBinaryFunctionOp<RandomAccessibleInterval<I1>, RandomAccessibleInterval<I2>, RandomAccessibleInterval<O>>
{

	@Override
	public RandomAccessibleInterval<O> compute2(
		RandomAccessibleInterval<I1> input1, RandomAccessibleInterval<I2> input2)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
