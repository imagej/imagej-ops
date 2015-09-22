
package net.imagej.ops.geometric;

import org.scijava.plugin.Plugin;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.MajorAxis;
import net.imagej.ops.Ops.Geometric2D.MinorMajorAxis;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

/**
 * Generic implementation of {@link MajorAxis}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Major Axis",
	name = Geometric2D.MajorAxis.NAME)
public class DefaultMajorAxis<O extends RealType<O>> extends
	AbstractGeometricFeature<Polygon, O>implements Geometric2D.MajorAxis
{

	@SuppressWarnings("rawtypes")
	private FunctionOp<Polygon, Pair> minorMajorAxisFunc;

	@Override
	public void initialize() {
		minorMajorAxisFunc = ops().function(MinorMajorAxis.class, Pair.class, in());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void compute(Polygon input, O output) {
		Pair<Double, Double> compute = minorMajorAxisFunc.compute(input);
		output.setReal(compute.getB());
	}
}
