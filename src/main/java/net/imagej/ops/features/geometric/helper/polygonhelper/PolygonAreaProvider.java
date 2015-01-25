package net.imagej.ops.features.geometric.helper.polygonhelper;

import net.imagej.ops.Op;
import net.imagej.ops.OpService;
import net.imagej.ops.features.Feature;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imagej.ops.statistics.geometric.GeometricStatOps.Area;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, name = "PolygonAreaProvider")
public class PolygonAreaProvider implements Feature {

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Parameter(type = ItemIO.INPUT)
	private Polygon in;

	@Parameter(type = ItemIO.INPUT)
	private OpService ops;

	@Override
	public void run() {
		out = ((DoubleType) ops.run(Area.class, in)).getRealDouble();
	}

	@Override
	public double getFeatureValue() {
		return out;
	}

}
