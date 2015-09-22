
package net.imagej.ops.geometric;

import org.scijava.plugin.Plugin;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.Elongation;
import net.imagej.ops.Ops.Geometric2D.MajorAxis;
import net.imagej.ops.Ops.Geometric2D.MinorAxis;

import net.imagej.ops.RTs;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.RealType;

/**
 * Generic implementation of {@link Elongation}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Elongation",
	name = Geometric2D.Elongation.NAME)
public class DefaultElongation<O extends RealType<O>> extends
	AbstractGeometricFeature<Polygon, O>implements Geometric2D.Elongation

{

	private FunctionOp<Polygon, O> minorAxisFunc;
	private FunctionOp<Polygon, O> majorAxisFunc;

	@Override
	public void initialize() {
		minorAxisFunc = RTs.function(ops(), MinorAxis.class, in());
		majorAxisFunc = RTs.function(ops(), MajorAxis.class, in());
	}

	@Override
	public void compute(Polygon input, O output) {
		output.setReal(1d - minorAxisFunc.compute(input).getRealDouble() /
			majorAxisFunc.compute(input).getRealDouble());
	}

}
