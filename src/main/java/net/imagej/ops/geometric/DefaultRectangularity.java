
package net.imagej.ops.geometric;

import org.scijava.plugin.Plugin;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.Area;
import net.imagej.ops.Ops.Geometric2D.Rectangularity;
import net.imagej.ops.Ops.Geometric2D.SmallestEnclosingRectangle;
import net.imagej.ops.RTs;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.RealType;

/**
 * Generic implementation of {@link Rectangularity}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Rectangularity",
	name = Geometric2D.Rectangularity.NAME)
public class DefaultRectangularity<O extends RealType<O>> extends
	AbstractGeometricFeature<Polygon, O>implements Geometric2D.Rectangularity
{

	private FunctionOp<Polygon, O> areaFunc;
	private FunctionOp<Polygon, Polygon> smallestEnclosingRectangleFunc;

	@Override
	public void initialize() {
		areaFunc = RTs.function(ops(), Area.class, in());
		smallestEnclosingRectangleFunc = ops().function(
			SmallestEnclosingRectangle.class, Polygon.class, Polygon.class);
	}

	@Override
	public void compute(Polygon input, O output) {
		output.setReal(areaFunc.compute(input).getRealDouble() / areaFunc.compute(
			smallestEnclosingRectangleFunc.compute(input)).getRealDouble());
	}

}
