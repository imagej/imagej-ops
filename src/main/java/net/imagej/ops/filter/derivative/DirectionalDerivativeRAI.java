package net.imagej.ops.filter.derivative;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Filter.DirectionalDerivative;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Calculates the derivative (with sobel kernel) of an image in a given dimension.
 * 
 * @author Eike Heinz, University of Konstanz
 *
 * @param <T> type of input
 */
@Plugin(type = Ops.Filter.DirectionalDerivative.class, name = Ops.Filter.DirectionalDerivative.NAME)
public class DirectionalDerivativeRAI<T extends RealType<T>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>
		implements DirectionalDerivative {

	@Parameter
	private int dimension;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> copyRAI;

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> createRAI;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> yConvolverRotated;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> xConvolverRotated;

	private BinaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> addOp;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> yConvolver;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> xConvolver;

	@Override
	public void initialize() {
		RandomAccessibleInterval<T> kernel = ops().create().kernelSobelSeparated();
		IntervalView<T> kernelX = Views.hyperSlice(Views.hyperSlice(kernel, 3, 0), 2, 0);
		IntervalView<T> kernelY = Views.hyperSlice(Views.hyperSlice(kernel, 3, 0), 2, 1);

		addOp = RAIs.binaryComputer(ops(), Ops.Math.Add.class, in(), in());

		yConvolver = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), kernelY);
		xConvolver = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), kernelX);

		copyRAI = RAIs.computer(ops(), Ops.Copy.RAI.class, in());
		createRAI = RAIs.function(ops(), Ops.Create.Img.class, in());

		yConvolverRotated = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), Views.rotate(kernelY, 0, 1));
		xConvolverRotated = RAIs.computer(ops(), Ops.Filter.Convolve.class, in(), Views.rotate(kernelX, 0, 1));
	}

	@Override
	public void compute1(RandomAccessibleInterval<T> input, RandomAccessibleInterval<T> output) {

		RandomAccessibleInterval<T> in = createRAI.compute1(input);
		copyRAI.compute1(input, in);

		for (int i = input.numDimensions() - 1; i >= 0; i--) {
			RandomAccessibleInterval<T> derivative = createRAI.compute1(input);
			if (i != 0) {
				if (dimension == i) {
					yConvolverRotated.compute1(Views.interval(Views.extendMirrorDouble(in), input), derivative);
				} else {
					xConvolverRotated.compute1(Views.interval(Views.extendMirrorDouble(in), input), derivative);
				}
			} else {
				if (dimension == i) {
					yConvolver.compute1(Views.interval(Views.extendMirrorDouble(in), input), derivative);
				} else {
					xConvolver.compute1(Views.interval(Views.extendMirrorDouble(in), input), derivative);
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
