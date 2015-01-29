package net.imagej.ops.geometric.point;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.geometric.point.GeometricPointOps.CenterOfGravityPoint;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imagej.ops.statistics.GeometricOps.Area;
import net.imglib2.RealPoint;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * 
 * Returns the center of gravity of a polygon.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = CenterOfGravityPoint.NAME)
public class DefaultCenterOfGravityOp implements CenterOfGravityPoint {

	@Parameter(type = ItemIO.OUTPUT)
	private RealPoint output;

	@Parameter(type = ItemIO.INPUT)
	private Polygon input;

	@Parameter
	private OpService ops;
	
	@Override
	public RealPoint getOutput() {
		return output;
	}

	@Override
	public void setOutput(RealPoint output) {
		this.output = output;
	}

	@Override
	public void run() {
		double area = ((DoubleType) ops.run(Area.class, input)).getRealDouble();
		
		double cx = 0;
		double cy = 0;
		for (int i = 0; i < input.size() - 1; i++) {

			RealPoint p0 = input.getPoint(i);
			RealPoint p1 = input.getPoint(i + 1);

			double p0_x = p0.getDoublePosition(0);
			double p0_y = p0.getDoublePosition(1);
			double p1_x = p1.getDoublePosition(0);
			double p1_y = p1.getDoublePosition(1);

			cx += (p0_x + p1_x) * (p0_x * p1_y - p1_x * p0_y);
			cy += (p0_y + p1_y) * (p0_x * p1_y - p1_x * p0_y);
		}

		cx = cx / 6d / area;
		cy = cy / 6d / area;

		output = new RealPoint(cx, cy);
	}

}
