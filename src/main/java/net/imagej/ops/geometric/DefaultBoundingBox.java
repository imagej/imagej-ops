
package net.imagej.ops.geometric;

import java.util.ArrayList;
import java.util.List;

import org.scijava.plugin.Plugin;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.Ops.Geometric2D;
import net.imagej.ops.Ops.Geometric2D.BoundingBox;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.geometric.Polygon;

/**
 * Generic implementation of {@link BoundingBox}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = GeometricOp.class, label = "Geometric: Bounding Box",
	name = Geometric2D.BoundingBox.NAME)
public class DefaultBoundingBox extends AbstractFunctionOp<Polygon, Polygon>
	implements GeometricOp<Polygon, Polygon>, Contingent, Geometric2D.BoundingBox {

	@Override
	public Polygon compute(Polygon input) {
		double min_x = Double.POSITIVE_INFINITY;
		double max_x = Double.NEGATIVE_INFINITY;
		double min_y = Double.POSITIVE_INFINITY;
		double max_y = Double.NEGATIVE_INFINITY;

		for (RealLocalizable rl : input.getVertices()) {
			if (rl.getDoublePosition(0) < min_x) {
				min_x = rl.getDoublePosition(0);
			}
			if (rl.getDoublePosition(0) > max_x) {
				max_x = rl.getDoublePosition(0);
			}
			if (rl.getDoublePosition(1) < min_y) {
				min_y = rl.getDoublePosition(1);
			}
			if (rl.getDoublePosition(1) > max_y) {
				max_y = rl.getDoublePosition(1);
			}
		}

		List<RealLocalizable> bounds = new ArrayList<RealLocalizable>();
		bounds.add(new RealPoint(min_x, min_y));
		bounds.add(new RealPoint(min_x, max_y));
		bounds.add(new RealPoint(max_x, max_y));
		bounds.add(new RealPoint(max_x, min_y));
		return new Polygon(bounds);
	}

	@Override
	public boolean conforms() {
		return 2 == in().numDimensions();
	}

}
