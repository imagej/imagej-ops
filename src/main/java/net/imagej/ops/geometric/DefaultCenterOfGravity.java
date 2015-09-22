
package net.imagej.ops.geometric;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.Centroid;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.geometric.Polygon;

/**
 * Generic implementation of {@link Centroid}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Center of Gravity",
	name = Geometric2D.Centroid.NAME)
public class DefaultCenterOfGravity extends
	AbstractFunctionOp<Polygon, RealLocalizable>implements
	GeometricOp<Polygon, RealLocalizable>, Contingent, Geometric2D.Centroid
{

	@Override
	public RealLocalizable compute(Polygon input) {


		double area = 0;
//		double area = ops().geometric.area(input);
		double cx = 0;
		double cy = 0;
		for (int i = 0; i < input.getVertices().size() - 1; i++) {
			RealLocalizable p0 = input.getVertices().get(i);
			RealLocalizable p1 = input.getVertices().get(i + 1);

			double p0_x = p0.getDoublePosition(0);
			double p0_y = p0.getDoublePosition(1);
			double p1_x = p1.getDoublePosition(0);
			double p1_y = p1.getDoublePosition(1);

			cx += (p0_x + p1_x) * (p0_x * p1_y - p1_x * p0_y);
			cy += (p0_y + p1_y) * (p0_x * p1_y - p1_x * p0_y);
		}

		return new RealPoint(cx / (area * 6), cy / (area * 6));
	}

	@Override
	public boolean conforms() {
		return 2 == in().numDimensions();
	}

}
