package net.imagej.ops.features.geometric.helper.polygonhelper;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.OutputOp;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imglib2.RealPoint;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "PolygonFeretProvider")
public class PolygonFeretOp implements OutputOp<Pair<RealPoint, RealPoint>> {

	@Parameter(type = ItemIO.OUTPUT)
	private Pair<RealPoint, RealPoint> output;

	@Parameter(type = ItemIO.INPUT)
	private Polygon in;

	@Parameter(type = ItemIO.INPUT)
	private OpService ops;

	@Override
	public void run() {
		
		double distance = Double.NEGATIVE_INFINITY;
		int in0 = -1;
		int in1 = -1;

		for (int i = 0; i < in.size(); i++) {
			for (int j = i + 1; j < in.size(); j++) {
				RealPoint temp0 = in.getPoint(i);
				RealPoint temp1 = in.getPoint(j);

				double sum = 0;
				for (int k = 0; k < temp0.numDimensions(); k++) {
					sum += Math.pow(
							temp0.getDoublePosition(k)
									- temp1.getDoublePosition(k), 2);
				}
				sum = Math.sqrt(sum);

				if (sum > distance) {
					distance = sum;
					in0 = i;
					in1 = j;
				}
			}
		}

		output = new ValuePair<RealPoint, RealPoint>(in.getPoint(in0), in.getPoint(in1));
	}

	@Override
	public Pair<RealPoint, RealPoint> getOutput() {
		return output;
	}

	@Override
	public void setOutput(Pair<RealPoint, RealPoint> output) {
		this.output = output;
	}

}
