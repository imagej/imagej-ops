package net.imagej.ops.geom.pixelbased;

import net.imagej.ops.OpService;
import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Geometric.VerticesCount;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.IterableInterval;
import net.imglib2.algorithm.neighborhood.DiamondShape;
import net.imglib2.algorithm.neighborhood.Shape;
import net.imglib2.roi.labeling.LabelRegion;
import net.imglib2.type.BooleanType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Geometric.Circularity.class, description = "Pixelbased Circularity.")
public class DefaultCircularityII
		extends AbstractUnaryHybridCF<LabelRegion<BitType>, DoubleType> implements VerticesCount {

	@Parameter(required = false)
	private Shape shape = new DiamondShape(1);

	@Parameter
	private OpService ops;

	@Override
	public DoubleType createOutput(LabelRegion<BitType> input) {
		return new DoubleType();
	}

	@Override
	public void compute(LabelRegion<BitType> input, DoubleType output) {
		output.set(ops.geom().size(input).get() * 4.0 * Math.PI / Math.pow(ops.geom().boundarySize(input).get(), 2));
	}

}
