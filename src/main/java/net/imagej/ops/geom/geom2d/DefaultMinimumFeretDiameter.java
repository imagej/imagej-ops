package net.imagej.ops.geom.geom2d;

import net.imagej.ops.Ops;
import net.imagej.ops.Ops.Geometric.MinimumFeretsDiameter;
import net.imagej.ops.special.function.Functions;
import net.imagej.ops.special.function.UnaryFunctionOp;
import net.imagej.ops.special.hybrid.AbstractUnaryHybridCF;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.real.DoubleType;
import net.imglib2.util.Pair;

import org.scijava.plugin.Plugin;

@Plugin(type = Ops.Geometric.MinimumFeretsDiameter.class)
public class DefaultMinimumFeretDiameter extends AbstractUnaryHybridCF<Polygon, DoubleType>
		implements MinimumFeretsDiameter {

	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<Polygon, Pair> minFeret;
	@SuppressWarnings("rawtypes")
	private UnaryFunctionOp<Pair, DoubleType> feretDiameter;

	@Override
	public void initialize() {
		minFeret = Functions.unary(ops(), Ops.Geometric.MinimumFeret.class, Pair.class, in());
		feretDiameter = Functions.unary(ops(), Ops.Geometric.FeretsDiameter.class, DoubleType.class, Pair.class);
	}

	@Override
	public void compute(Polygon input, DoubleType output) {
		output.set(feretDiameter.calculate(minFeret.calculate(input)).get());
	}

	@Override
	public DoubleType createOutput(Polygon input) {
		return new DoubleType();
	}
}
