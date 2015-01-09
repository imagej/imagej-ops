package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.geometric.GeometricFeatures.CircularityFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonAreaProvider;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonPerimeterProvider;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link CircularityFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = CircularityFeature.NAME)
public class DefCircularity implements CircularityFeature {

	@Parameter(type = ItemIO.INPUT)
	private PolygonAreaProvider area;

	@Parameter(type = ItemIO.INPUT)
	private PolygonPerimeterProvider perimter;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		out = 4
				* Math.PI
				* (area.getFeatureValue() / Math.pow(
						perimter.getFeatureValue(), 2));
	}

}
