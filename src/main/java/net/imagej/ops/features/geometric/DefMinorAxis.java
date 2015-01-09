package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.geometric.GeometricFeatures.MinorAxisFeature;
import net.imagej.ops.geometric.polygon.GeometricPolygonOps.SmallestEnclosingRectanglePolygon;
import net.imagej.ops.geometric.polygon.Polygon;
import net.imglib2.RealPoint;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link MinorAxisFeature}. Use {@link FeatureService} to
 * compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = MinorAxisFeature.NAME)
public class DefMinorAxis implements MinorAxisFeature {

	@Parameter(type = ItemIO.INPUT)
	private SmallestEnclosingRectanglePolygon polygon;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		Polygon input = polygon.getOutput();

		RealPoint origin = input.getPoint(0);
		RealPoint diagonal = input.getPoint(2);

		out = Math.min(
				Math.abs(origin.getDoublePosition(0)
						- diagonal.getDoublePosition(0)),
				Math.abs(origin.getDoublePosition(1)
						- diagonal.getDoublePosition(1)));
	}

}
