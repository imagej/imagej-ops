package net.imagej.ops.filter.derivative;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Filter.PartialDerivative;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.FinalInterval;
import net.imglib2.Interval;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.IntervalView;
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
@Plugin(type = Ops.Filter.PartialDerivative.class, name = Ops.Filter.PartialDerivative.NAME)
public class PartialDerivativeRAI<T extends RealType<T>> extends
		AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> implements PartialDerivative {

	@Parameter
	private int dimension;

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> createRAI;

	private BinaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> addOp;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> kernelBConvolver;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>[] kernelAConvolverArray;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		RandomAccessibleInterval<T> kernel = ops().create().kernelSobelSeparated();
		// kernel A contains 1 2 1
		//TODO randomaccessibleinterval?
		IntervalView<T> kernelA = Views.hyperSlice(Views.hyperSlice(kernel, 3, 0), 2, 0);
		// kernel B contains -1 0 1
		IntervalView<T> kernelB = Views.hyperSlice(Views.hyperSlice(kernel, 3, 0), 2, 1);

		// add dimensions to kernel if input has more than 2 dimensions to
		// properly rotate the kernel
		if (in().numDimensions() > 2) {
			RandomAccessible<T> expandedKernelA = Views.addDimension(kernelA);
			RandomAccessible<T> expandedKernelB = Views.addDimension(kernelB);
			for (int i = 0; i < in().numDimensions() - 3; i++) {
				expandedKernelA = Views.addDimension(expandedKernelA);
				expandedKernelB = Views.addDimension(expandedKernelB);
			}
			long[] dims = new long[in().numDimensions()];
			for (int j = 0; j < in().numDimensions(); j++) {
				dims[j] = 1;
			}
			dims[0] = 3;
			Interval kernelInterval = new FinalInterval(dims);
			kernelA = Views.interval(expandedKernelA, kernelInterval);
			kernelB = Views.interval(expandedKernelB, kernelInterval);
		}

		long[] dims = new long[in().numDimensions()];
		if (dimension == 0) {
			kernelBConvolver = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), kernelB);
		} else {
			// rotate kernel B to dimension
			for (int j = 0; j < in().numDimensions(); j++) {
				if (j == dimension) {
					dims[j] = 3;
				} else {
					dims[j] = 1;
				}
			}

			Img<DoubleType> kernelInterval = ops().create().img(dims);
			// rotate kernelB to required dimension
			IntervalView<T> rotatedKernelB = kernelB;
			for (int i = 0; i < dimension; i++) {
				rotatedKernelB = Views.rotate(rotatedKernelB, i, i + 1);
			}

			rotatedKernelB = Views.interval(rotatedKernelB, kernelInterval);
			kernelBConvolver = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), rotatedKernelB);
		}

		dims = null;

		// rotate kernel A to all other dimensions
		kernelAConvolverArray = new UnaryComputerOp[in().numDimensions()];
		if (dimension != 0) {
			kernelAConvolverArray[0] = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), kernelA);
		}
		IntervalView<T> rotatedKernelA = kernelA;
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
				Img<DoubleType> kernelInterval = ops().create().img(dims);
				for (int j = 0; j < i; j++) {
					rotatedKernelA = Views.rotate(rotatedKernelA, j, j + 1);
				}

				kernelAConvolverArray[i] = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(),
						Views.interval(rotatedKernelA, kernelInterval));
				rotatedKernelA = kernelA;
			}
		}

		addOp = RAIs.binaryComputer(ops(), Ops.Math.Add.class, in(), in());
		createRAI = RAIs.function(ops(), Ops.Create.Img.class, in());
	}

	@Override
	public void compute1(RandomAccessibleInterval<T> input, RandomAccessibleInterval<T> output) {
		RandomAccessibleInterval<T> in = input;
		for (int i = input.numDimensions() - 1; i >= 0; i--) {
			RandomAccessibleInterval<T> derivative = createRAI.compute1(input);
			if (dimension == i) {
				kernelBConvolver.compute1(Views.interval(Views.extendMirrorDouble(in), input), derivative);
			} else {
				kernelAConvolverArray[i].compute1(Views.interval(Views.extendMirrorDouble(in), input), derivative);
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
