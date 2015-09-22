
package net.imagej.ops.geometric;

import org.scijava.plugin.Plugin;

import net.imagej.ops.FunctionOp;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.Area;
import net.imagej.ops.Ops.Geometric2D.Circularity;
import net.imagej.ops.Ops.Geometric2D.Perimeter;
import net.imagej.ops.RTs;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.RealType;

/**
 * Generic implementation of {@link Circularity}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Circularity",
	name = Geometric2D.Circularity.NAME)
public class DefaultCircularity<O extends RealType<O>> extends
	AbstractGeometricFeature<Polygon, O>implements Geometric2D.Circularity
{

	private FunctionOp<Polygon, O> areaFunc;
	private FunctionOp<Polygon, O> perimiterFunc;

	@Override
	public void initialize() {
		areaFunc = RTs.function(ops(), Area.class, in());
		perimiterFunc = RTs.function(ops(), Perimeter.class, in());
	}

	@Override
	public void compute(Polygon input, O output) {
		output.setReal(4 * Math.PI * (areaFunc.compute(input).getRealDouble() / Math
			.pow(perimiterFunc.compute(input).getRealDouble(), 2)));
	}

}
