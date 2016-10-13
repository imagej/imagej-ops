package net.imagej.ops.geom.geom2d;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Geometric.MaximumFeretsDiameter;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Geometric.MaximumFeretsDiameter.class)
public class DefaultMaximumFeretDiameter extends AbstractUnaryHybridCF<Polygon, DoubleType>
		implements MaximumFeretsDiameter {

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<Polygon, Pair> maxFeret;
	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<Pair, DoubleType> feretDiameter;

	@Override
	public void initialize() {
		maxFeret = Functions.unary(ops(), Ops.Geometric.MaximumFeret.class, Pair.class, in());
		feretDiameter = Functions.unary(ops(), Ops.Geometric.FeretsDiameter.class, DoubleType.class, Pair.class);
	}

	@Override
	public void compute1(Polygon input, DoubleType output) {
		output.set(feretDiameter.compute1(maxFeret.compute1(input)).get());
	}

	@Override
	public DoubleType createOutput(Polygon input) {
		return new DoubleType();
	}
}
