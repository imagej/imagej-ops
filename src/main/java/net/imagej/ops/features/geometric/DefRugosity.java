package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.geometric.GeometricFeatures.ConvexityFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RugosityFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonConvexHullPerimeterProvider;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonPerimeterProvider;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link ConvexityFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = RugosityFeature.NAME)
public class DefRugosity implements RugosityFeature {

	@Parameter(type = ItemIO.INPUT)
	private PolygonPerimeterProvider perimter;

	@Parameter(type = ItemIO.INPUT)
	private PolygonConvexHullPerimeterProvider convexHullPerimeter;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		out = perimter.getFeatureValue()
				/ convexHullPerimeter.getFeatureValue();
	}

}
