
package net.imagej.ops.geometric;

import org.scijava.plugin.Plugin;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.Eccentricity;
import net.imagej.ops.Ops.Geometric2D.MajorAxis;
import net.imagej.ops.Ops.Geometric2D.MinorAxis;

import net.imagej.ops.RTs;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.RealType;

/**
 * Generic implementation of {@link Eccentricity}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Eccentricity",
	name = Geometric2D.Eccentricity.NAME)
public class DefaultEccentricity<O extends RealType<O>> extends
	AbstractGeometricFeature<Polygon, O>implements Geometric2D.Eccentricity

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
		output.setReal(majorAxisFunc.compute(input).getRealDouble() / minorAxisFunc
			.compute(input).getRealDouble());
	}

}
