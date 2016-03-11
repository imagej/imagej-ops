package net.imagej.ops.filter.derivative;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Filter.DirectionalDerivative;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Calculates the derivative (with sobel kernel) of an image in a given
 * dimension.
 * 
 * @author Eike Heinz, University of Konstanz
 *
 * @param <T>
 *            type of input
 */
@Plugin(type = Ops.Filter.DirectionalDerivative.class, name = Ops.Filter.DirectionalDerivative.NAME)
public class DirectionalDerivativeRAI<T extends RealType<T>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
		implements DirectionalDerivative {

	@Parameter
	private int dimension;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> copyRAI;

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> createRAI;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> kernelBConvolverRotated;

	private BinaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> addOp;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> kernelBConvolver;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> kernelAConvolver;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>[] kernelAConvolverRotatedArray;

	@Override
	public void initialize() {
		RandomAccessibleInterval<T> kernel = ops().create().kernelSobelSeparated();
		// kernel A contains 1 2 1
		IntervalView<T> kernelA = Views.hyperSlice(Views.hyperSlice(kernel, 3, 0), 2, 0);
		// kernel B contains -1 0 1
		IntervalView<T> kernelB = Views.hyperSlice(Views.hyperSlice(kernel, 3, 0), 2, 1);

		IntervalView<T> finalExpandedKernelA = kernelA;
		IntervalView<T> finalExpandedKernelB = kernelB;

		// add dimensions to kernel if input has more than 2 dimensions to
		// properly rotate the kernel
		if (in().numDimensions() > 2) {
			MixedTransformView<T> expandedKernelA = Views.addDimension(kernelA);
			MixedTransformView<T> expandedKernelB = Views.addDimension(kernelB);
			for (int i = 0; i < in().numDimensions() - 3; i++) {
				expandedKernelA = Views.addDimension(expandedKernelA);
				expandedKernelB = Views.addDimension(expandedKernelB);
			}
			long[] dims = new long[in().numDimensions()];
			for (int j = 0; j < in().numDimensions(); j++) {
				dims[j] = 1;
			}
			dims[0] = 3;
			Img<DoubleType> tempInterval = ops().create().img(dims);
			finalExpandedKernelA = Views.interval(expandedKernelA, tempInterval);
			finalExpandedKernelB = Views.interval(expandedKernelB, tempInterval);
		}

		// rotate kernel B to dimension
		long[] dims = new long[in().numDimensions()];
		for (int j = 0; j < in().numDimensions(); j++) {
			dims[j] = 1;
		}
		dims[dimension] = 3;
		Img<DoubleType> kernelInterval = ops().create().img(dims);
		IntervalView<T> rotatedKernelB = Views.interval(Views.rotate(finalExpandedKernelB, 0, dimension),
				kernelInterval);
		dims = null;

		// rotate kernel A to all other dimensions
		IntervalView<T>[] rotatedKernelAArray = new IntervalView[in().numDimensions()];
		for (int i = 1; i < in().numDimensions(); i++) {
			if (i != dimension) {
				dims = new long[in().numDimensions()];
				for (int j = 0; j < in().numDimensions(); j++) {
					if (i == j) {
						dims[j] = 3;
					} else {
						dims[j] = 1;
					}
				}
				kernelInterval = ops().create().img(dims);
				rotatedKernelAArray[i] = Views.interval(Views.rotate(finalExpandedKernelA, 0, i), kernelInterval);
			}
		}

		addOp = RAIs.binaryComputer(ops(), Ops.Math.Add.class, in(), in());

		kernelBConvolver = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), kernelB);
		kernelAConvolver = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), kernelA);

		copyRAI = RAIs.computer(ops(), Ops.Copy.RAI.class, in());
		createRAI = RAIs.function(ops(), Ops.Create.Img.class, in());

		kernelBConvolverRotated = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), rotatedKernelB);

		kernelAConvolverRotatedArray = new UnaryComputerOp[in().numDimensions()];
		for (int i = 1; i < in().numDimensions(); i++) {
			if (i != dimension) {
				kernelAConvolverRotatedArray[i] = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(),
						rotatedKernelAArray[i]);
			}
		}
	}

	@Override
	public void compute1(RandomAccessibleInterval<T> input, RandomAccessibleInterval<T> output) {

		RandomAccessibleInterval<T> in = createRAI.compute1(input);
		copyRAI.compute1(input, in);

		for (int i = input.numDimensions() - 1; i >= 0; i--) {
			RandomAccessibleInterval<T> derivative = createRAI.compute1(input);
			if (i != 0) {
				if (dimension == i) {
					kernelBConvolverRotated.compute1(Views.interval(Views.extendMirrorDouble(in), input), derivative);
				} else {
					kernelAConvolverRotatedArray[i].compute1(Views.interval(Views.extendMirrorDouble(in), input),
							derivative);
				}
			} else {
				if (dimension == i) {
					kernelBConvolver.compute1(Views.interval(Views.extendMirrorDouble(in), input), derivative);
				} else {
					kernelAConvolver.compute1(Views.interval(Views.extendMirrorDouble(in), input), derivative);
				}
			}
			in = derivative;
		}
		addOp.compute2(output, in, output);
	}

	@Override
	public RandomAccessibleInterval<T> createOutput(RandomAccessibleInterval<T> input) {
		return createRAI.compute1(input);
	}
}
