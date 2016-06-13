
package net.imagej.ops.learning;

import net.imagej.ops.AbstractNamespace;
import net.imagej.ops.Namespace;
import net.imagej.ops.OpMethod;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;

import org.scijava.plugin.Plugin;

/**
 * The learning namespace contains ops that learn.
 * 
 * @author Brian Northan
 */
@Plugin(type = Namespace.class)
public class LearningNamespace extends AbstractNamespace {

	@SuppressWarnings("unchecked")
	@OpMethod(
		op = net.imagej.ops.learning.neuralnet.DefaultConvolutionLayerBackprop.class)
	public <
		I1 extends ComplexType<I1>, I2 extends ComplexType<I2>, O extends ComplexType<O>>
		RandomAccessibleInterval<O> convolutionLayerBackProp(
			final RandomAccessibleInterval<I1> in1, final RandomAccessibleInterval<I2> in2)
	{
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.learning.neuralnet.DefaultConvolutionLayerBackprop.class,
				in1, in2);
		return result;
	}

	@SuppressWarnings("unchecked")
	@OpMethod(
		op = net.imagej.ops.learning.neuralnet.DefaultConvolutionLayerBackprop.class)
	public <
		I1 extends ComplexType<I1>, I2 extends ComplexType<I2>, O extends ComplexType<O>>
		RandomAccessibleInterval<O> convolutionLayerBackProp(
			final RandomAccessibleInterval<I1> in1,
			final RandomAccessibleInterval<I2> in2, final Type<O> outType)
	{
		final RandomAccessibleInterval<O> result =
			(RandomAccessibleInterval<O>) ops().run(
				net.imagej.ops.learning.neuralnet.DefaultConvolutionLayerBackprop.class,
				in1, in2, outType);
		return result;
	}

//-- Namespace methods --

	@Override
	public String getName() {
		return "learning";
	}

}
