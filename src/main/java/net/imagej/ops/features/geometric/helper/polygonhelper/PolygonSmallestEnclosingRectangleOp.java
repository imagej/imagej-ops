package net.imagej.ops.features.geometric.helper.polygonhelper;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.OutputOp;
import net.imagej.ops.geometric.polygon.GeometricPolygonOps.SmallestEnclosingRectanglePolygon;
import net.imagej.ops.geometric.polygon.Polygon;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "SmallestEnclosingRectangleProvider")
public class PolygonSmallestEnclosingRectangleOp implements OutputOp<Polygon> {

	@Parameter(type = ItemIO.OUTPUT)
	private Polygon out;

	@Parameter(type = ItemIO.INPUT)
	private Polygon in;

	@Parameter(type = ItemIO.INPUT)
	private OpService ops;

	@Override
	public void run() {
		out = (Polygon) ops.run(SmallestEnclosingRectanglePolygon.class, in);
	}

	@Override
	public Polygon getOutput() {
		return out;
	}

	@Override
	public void setOutput(Polygon output) {
		this.out = output;
	}

}
