
package net.imagej.ops.learning.neuralnet;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.special.function.AbstractBinaryFunctionOp;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.real.FloatType;

import org.scijava.Priority;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Learning.ConvolutionLayerBackProp.class,
	priority = Priority.HIGH_PRIORITY)
public class DefaultConvolutionLayerBackprop<I1 extends NativeType<I1> & ComplexType<I1>, I2 extends NativeType<I2> & ComplexType<I2>, O extends NativeType<O> & ComplexType<O>>
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
			Object temp = new FloatType();
			outType = (Type<O>) temp;
		}

		// initialize kernel
		RandomAccessibleInterval<O> out = ops.create().kernelGauss(1., 2, outType
			.createVariable());

		// TODO Auto-generated method stub
		return out;
	}

}
