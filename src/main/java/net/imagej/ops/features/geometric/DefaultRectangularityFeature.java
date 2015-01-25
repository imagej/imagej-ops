package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.geometric.GeometricFeatures.RectangularityFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonAreaProvider;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonSmallestEnclosingRectangleAreaProvider;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link RectangularityFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = RectangularityFeature.NAME)
public class DefaultRectangularityFeature implements RectangularityFeature {

	@Parameter(type = ItemIO.INPUT)
	private PolygonAreaProvider area;

	@Parameter(type = ItemIO.INPUT)
	private PolygonSmallestEnclosingRectangleAreaProvider serArea;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		out = area.getFeatureValue() / serArea.getFeatureValue();
	}

}
