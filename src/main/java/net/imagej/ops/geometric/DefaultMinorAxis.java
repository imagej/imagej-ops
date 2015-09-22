
package net.imagej.ops.geometric;

import org.scijava.plugin.Plugin;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.MinorAxis;
import net.imagej.ops.Ops.Geometric2D.MinorMajorAxis;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Pair;

/**
 * Generic implementation of {@link MinorAxis}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Minor Axis",
	name = Geometric2D.MinorAxis.NAME)
public class DefaultMinorAxis<O extends RealType<O>> extends
	AbstractGeometricFeature<Polygon, O>implements Geometric2D.MinorAxis
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
		output.setReal(compute.getA());
	}
}
