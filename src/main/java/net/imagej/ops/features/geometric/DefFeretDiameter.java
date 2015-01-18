package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsDiameterFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonFeretProvider;
import net.imglib2.RealPoint;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link FeretsDiameterFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = FeretsDiameterFeature.NAME)
public class DefFeretDiameter implements FeretsDiameterFeature {

	@Parameter(type = ItemIO.INPUT)
	private PolygonFeretProvider feretResult;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {

		RealPoint p1 = feretResult.getOutput().getA();
		RealPoint p2 = feretResult.getOutput().getB();

		out = Math.hypot(p1.getDoublePosition(0) - p2.getDoublePosition(0),
				p1.getDoublePosition(1) - p2.getDoublePosition(1));
	}
}
