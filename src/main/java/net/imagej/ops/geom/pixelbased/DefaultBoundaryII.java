package net.imagej.ops.geom.pixelbased;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Geometric.VerticesCount;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.outofbounds.OutOfBoundsBorderFactory;
import net.imglib2.outofbounds.OutOfBoundsConstantValueFactory;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.logic.BoolType;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.view.Views;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Geometric.Boundary.class, description = "Pixelbased Boundary.")
public class DefaultBoundaryII<T extends BooleanType<T>> extends
		AbstractUnaryHybridCF<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> implements VerticesCount {

	@Parameter(required = false)
	private Shape shape = new DiamondShape(1);

	@Parameter
	private OpService ops;

	private UnaryFunctionOp<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>> createII;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize() {
		createII = (UnaryFunctionOp) Functions.unary(ops(), Ops.Create.Img.class, RandomAccessibleInterval.class, in(),
				in().randomAccess().get());
	}

	@Override
	public RandomAccessibleInterval<T> createOutput(RandomAccessibleInterval<T> input) {
		return createII.calculate(input);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute(RandomAccessibleInterval<T> input, RandomAccessibleInterval<T> output) {
		final IterableInterval<T> dilated = ops.morphology().dilate(input, shape);

		input.randomAccess().get();

		ops.map(output, input, dilated, (BinaryComputerOp<T, T, T>) ops.op(Ops.Math.Xor.class,
				output.randomAccess().get(), input.randomAccess().get(), dilated.firstElement()));
	}

}
