
package net.imagej.ops.geometric;

import org.scijava.plugin.Plugin;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.Area;
import net.imagej.ops.Ops.Geometric2D.ConvexHull;
import net.imagej.ops.Ops.Geometric2D.Solidity;
import net.imagej.ops.RTs;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.RealType;

/**
 * Generic implementation of {@link Solidity}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Solidity",
	name = Geometric2D.Solidity.NAME)
public class DefaultSolidity<O extends RealType<O>> extends
	AbstractGeometricFeature<Polygon, O>implements Geometric2D.Solidity
{

	private FunctionOp<Polygon, O> areaFunc;
	private FunctionOp<Polygon, Polygon> convexHullFunc;

	@Override
	public void initialize() {
		areaFunc = RTs.function(ops(), Area.class, in());
		convexHullFunc = ops().function(ConvexHull.class, Polygon.class, in());
	}

	@Override
	public void compute(Polygon input, O output) {
		output.setReal(areaFunc.compute(input).getRealDouble() /
			areaFunc.compute(convexHullFunc.compute(input)).getRealDouble());
	}

}
