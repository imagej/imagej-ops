
package net.imagej.ops.geometric;

import org.scijava.plugin.Plugin;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.ConvexHull;
import net.imagej.ops.Ops.Geometric2D.Convexity;
import net.imagej.ops.Ops.Geometric2D.Perimeter;
import net.imagej.ops.RTs;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.RealType;

/**
 * Generic implementation of {@link Convexity}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Convexity",
	name = Geometric2D.Convexity.NAME)
public class DefaultConvexity<O extends RealType<O>> extends
	AbstractGeometricFeature<Polygon, O>implements Geometric2D.Convexity
{

	private FunctionOp<Polygon, Polygon> convexHullFunction;
	private FunctionOp<Polygon, O> perimiterFunc;

	@Override
	public void initialize() {
		convexHullFunction = ops().function(ConvexHull.class, Polygon.class,
			Polygon.class);
		perimiterFunc = RTs.function(ops(), Perimeter.class, in());
	}

	@Override
	public void compute(Polygon input, O output) {

		// get perimeter of input and its convex hull
		O inputArea = perimiterFunc.compute(input);
		O convexHullArea = perimiterFunc.compute(convexHullFunction.compute(input));


		output.setReal(convexHullArea.getRealDouble() / inputArea.getRealDouble());
	}

}
