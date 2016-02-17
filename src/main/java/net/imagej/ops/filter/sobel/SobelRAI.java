package net.imagej.ops.filter.sobel;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Filter.Sobel;
import net.imagej.ops.Ops.Math.Sqr;
import net.imagej.ops.Ops.Math.Sqrt;
import net.imagej.ops.special.chain.RAIs;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Plugin;

/**
 * Simple sobel filter implementation using separated sobel kernel.
 * 
 * @author Eike Heinz, University of Konstanz
 *
 * @param <T> type of input
 */

@Plugin(type = Ops.Filter.Sobel.class, name = Ops.Filter.Sobel.NAME)
public class SobelRAI<T extends RealType<T>>
		extends AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> implements Sobel {

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> createRAIFromRAI;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> squareMapOp;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> sqrtMapOp;

	private BinaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> addOp;

	private UnaryComputerOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>[] derivativeComputers;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize() {
		createRAIFromRAI = RAIs.function(ops(), Ops.Create.Img.class, in());

		Sqr squareOp = ops().op(Ops.Math.Sqr.class, RealType.class, RealType.class);
		squareMapOp = RAIs.computer(ops(), Ops.Map.class, in(), squareOp);
		Sqrt sqrtOp = ops().op(Ops.Math.Sqrt.class, RealType.class, RealType.class);
		sqrtMapOp = RAIs.computer(ops(), Ops.Map.class, in(), sqrtOp);
		addOp = RAIs.binaryComputer(ops(), Ops.Math.Add.class, in(), in());

		derivativeComputers = new UnaryComputerOp[in().numDimensions()];
		for (int i = 0; i < in().numDimensions(); i++) {
			derivativeComputers[i] = RAIs.computer(ops(), Ops.Filter.DirectionalDerivative.class, in(), i);
		}

	}

	@Override
	public void compute1(RandomAccessibleInterval<T> input, RandomAccessibleInterval<T> output) {

		for (int i = 0; i < derivativeComputers.length; i++) {
			RandomAccessibleInterval<T> derivative = createRAIFromRAI.compute1(input);
			derivativeComputers[i].compute1(input, derivative);
			squareMapOp.compute1(derivative, derivative);
			addOp.compute2(output, derivative, output);
		}
		sqrtMapOp.compute1(output, output);
	}

	@Override
	public RandomAccessibleInterval<T> createOutput(RandomAccessibleInterval<T> input) {
		return createRAIFromRAI.compute1(input);
	}
}
