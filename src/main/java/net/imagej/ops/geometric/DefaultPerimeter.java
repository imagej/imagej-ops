
package net.imagej.ops.geometric;

import org.scijava.plugin.Plugin;

import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.Perimeter;
import net.imglib2.roi.geometric.Polygon;
import net.imglib2.type.numeric.RealType;


/**
 * Generic implementation of {@link Perimeter}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Perimeter",
	name = Geometric2D.Perimeter.NAME)
public class DefaultPerimeter<O extends RealType<O>> extends
	AbstractGeometricFeature<Polygon, O>implements Geometric2D.Perimeter
{

	@Override
	public void compute(Polygon input, O output) {
		double perimeter = 0;
		for (int i = 0; i < input.getVertices().size(); i++) {
			int nexti = i + 1;
			if (nexti == input.getVertices().size()) nexti = 0;

			double dx2 = input.getVertices().get(nexti).getDoublePosition(0) - input
				.getVertices().get(i).getDoublePosition(0);
			double dy2 = input.getVertices().get(nexti).getDoublePosition(1) - input
				.getVertices().get(i).getDoublePosition(1);

			perimeter += Math.sqrt(Math.pow(dx2, 2) + Math.pow(dy2, 2));
		}

		output.setReal(perimeter);
	}

}
