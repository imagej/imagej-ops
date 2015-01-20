
package net.imagej.ops.fft.filter;

import org.scijava.plugin.Parameter;

import net.imagej.ops.AbstractOutputFunction;
import net.imagej.ops.OpService;
import net.imglib2.Interval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.planar.PlanarImgFactory;
import net.imglib2.outofbounds.OutOfBoundsFactory;
import net.imglib2.type.Type;
import net.imglib2.type.numeric.ComplexType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.util.Util;

/**
 * Abstract class for FFT based filters that operate on Img.
 * 
 * @author bnorthan
 * @param <I>
 * @param <O>
 * @param <K>
 * @param <C>
 */
public abstract class AbstractFFTFilterImg<I extends RealType<I>, O extends RealType<O>, K extends RealType<K>, C extends ComplexType<C>>
	extends AbstractOutputFunction<Img<I>, Img<O>>
{

	@Parameter
	protected OpService ops;

	/**
	 * the kernel (psf)
	 */
	@Parameter
	protected RandomAccessibleInterval<K> kernel;

	/**
	 * Border size in each dimensions. If null default border size will be added.
	 */
	@Parameter(required = false)
	protected long[] borderSize = null;

	/**
	 * generates the out of bounds strategy for the extended area of the input
	 */
	@Parameter(required = false)
	protected OutOfBoundsFactory<I, RandomAccessibleInterval<I>> obfInput;

	/**
	 * generates the out of bounds strategy for the extended area of the kernel
	 */
	@Parameter(required = false)
	protected OutOfBoundsFactory<K, RandomAccessibleInterval<K>> obfKernel;

	/**
	 * The output type. If null default output type will be used.
	 */
	@Parameter(required = false)
	protected Type<O> outType;

	/**
	 * Factory to create output Img
	 */
	@Parameter(required = false)
	protected ImgFactory<O> outFactory;

	/**
	 * FFT type
	 */
	@Parameter(required = false)
	protected ComplexType<C> fftType;

	/**
	 * Factory to create ffts Imgs
	 */
	@Parameter(required = false)
	protected ImgFactory<C> fftFactory;

	/**
	 * Create the output using the outFactory and outType if they exist. If these
	 * are null use a default factory and type
	 */
	@Override
	public Img<O> createOutput(Img<I> input) {

		// if the outType is null
		if (outType == null) {

			// if the input type and kernel type are the same use this type
			if (input.firstElement().getClass() == Util.getTypeFromInterval(kernel)
				.getClass())
			{
				Object temp = input.firstElement().createVariable();
				outType = (Type<O>) temp;

			}
			// otherwise default to float
			else {
				Object temp = new FloatType();
				outType = (Type<O>) temp;
			}
		}

		// if the outFactory is null use a PlanarImgFactory to create the output
		if (outFactory == null) {
			Object temp = new PlanarImgFactory();
			outFactory = (ImgFactory<O>) temp;
		}

		return outFactory.create(input, outType.createVariable());
	}

	/**
	 * 
	 */
	@Override
	public Img<O> safeCompute(Img<I> input, Img<O> output) {

		// run the op that extends the input and kernel and creates the Imgs
		// required for the fft algorithm
		CreateFFTFilterMemory<I, O, K, C> createMemory =
			ops.op(CreateFFTFilterMemory.class, input, kernel, borderSize);

		createMemory.run();

		// run the filter, pass in the memory created above
		runFilter(createMemory.getRAIExtendedInput(), createMemory
			.getRAIExtendedKernel(), createMemory.getFFTImg(), createMemory
			.getFFTKernel(), output, createMemory.getImgConvolutionInterval());

		return output;
	}

	/**
	 * This function is called after the rais and ffts are set up and implements a frequency filter.  
	 * 
	 * @param raiExtendedInput
	 * @param raiExtendedKernel
	 * @param fftImg
	 * @param fftKernel
	 * @param output
	 * @param imgConvolutionInterval
	 */
	abstract public void runFilter(RandomAccessibleInterval<I> raiExtendedInput,
		RandomAccessibleInterval<K> raiExtendedKernel, Img<C> fftImg,
		Img<C> fftKernel, Img<O> output, Interval imgConvolutionInterval);

}
