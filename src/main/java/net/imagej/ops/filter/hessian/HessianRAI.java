
package net.imagej.ops.filter.hessian;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Filter.Hessian;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.Dimensions;
import net.imglib2.FinalDimensions;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Util;
import net.imglib2.view.IntervalView;
import net.imglib2.view.Views;

import org.scijava.plugin.Plugin;

/**
 * Hessian filter using the sobel filter with separated kernel.
 * 
 * @author Eike Heinz
 *
 * @param <T> type of input
 */

@Plugin(type = Ops.Filter.Hessian.class, name = Ops.Filter.Hessian.NAME)
public class HessianRAI<T extends RealType<T>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> implements Hessian {

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>[] derivativeComputers;

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<Dimensions, RandomAccessibleInterval> createRAIFromDim;

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> createRAIFromRAI;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		createRAIFromDim = Functions.unary(ops(), Ops.Create.Img.class, RandomAccessibleInterval.class,
				Dimensions.class, Util.getTypeFromInterval(in()).createVariable());
		createRAIFromRAI = RAIs.function(ops(), Ops.Create.Img.class, in());

		derivativeComputers = new UnaryComputerOp[in().numDimensions()];
		for (int i = 0; i < in().numDimensions(); i++) {
			derivativeComputers[i] = RAIs.computer(ops(), Ops.Filter.DirectionalDerivative.class, in(), i);
		}
	}

	@Override
	public void compute1(RandomAccessibleInterval<T> input, RandomAccessibleInterval<T> output) {
		int iteration = 0;
		
		for (int i = 0; i < input.numDimensions(); i++) {
			RandomAccessibleInterval<T> derivative = createRAIFromRAI.compute1(input);
			derivativeComputers[i].compute1(input, derivative);
			for (int j = 0; j < input.numDimensions(); j++) {
				IntervalView<T> out = Views.hyperSlice(Views.hyperSlice(output, 3, 0), 2, iteration);
				derivativeComputers[j].compute1(derivative, out);
				iteration++;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public RandomAccessibleInterval<T> createOutput(RandomAccessibleInterval<T> input) {
		long[] dims = new long[input.numDimensions() + 2];
		for (int i = 0; i < dims.length - 1; i++) {
			dims[i] = input.dimension(i);
		}
		dims[dims.length - 1] = input.numDimensions() * input.numDimensions();
		Dimensions dim = FinalDimensions.wrap(dims);
		return createRAIFromDim.compute1(dim);
	}
}
