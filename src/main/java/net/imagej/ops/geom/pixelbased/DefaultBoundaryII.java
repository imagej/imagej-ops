package net.imagej.ops.geom.pixelbased;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Geometric.VerticesCount;
import net.imagej.ops.special.computer.BinaryComputerOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.IterableInterval;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.roi.IterableRegion;
import net.imglib2.roi.Regions;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.roi.util.IterableRandomAccessibleRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.Type;
import net.imglib2.type.logic.BoolType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Geometric.Boundary.class, description = "Pixelbased Boundary.")
public class DefaultBoundaryII<T extends Type<T>> extends AbstractUnaryHybridCF<LabelRegion<T>, IterableInterval<T>>
		implements VerticesCount {

	@Parameter(required = false)
	private Shape shape = new DiamondShape(1);

	@Parameter
	private OpService ops;

	@SuppressWarnings("unchecked")
	@Override
	public IterableInterval<T> createOutput(LabelRegion<T> input) {
		return (IterableInterval<T>) Regions.iterable(input);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute(LabelRegion<T> input, IterableInterval<T> output) {

		final IterableRegion<T> iterable = (IterableRegion<T>) IterableRandomAccessibleRegion.create(input);

		final IterableInterval<T> dilated = ops.morphology().dilate((RandomAccessibleInterval<T>) iterable, shape);

		ops.map(output, iterable, dilated, (BinaryComputerOp<BoolType, BoolType, BooleanType>) ops
				.op(Ops.Math.Xor.class, iterable.firstElement(), dilated.firstElement(), output.firstElement()));
	}

}
