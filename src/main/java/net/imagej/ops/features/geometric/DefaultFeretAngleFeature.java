package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.geometric.GeometricFeatures.FeretsAngleFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.PolygonFeretProvider;
import net.imglib2.RealPoint;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link FeretsAngleFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = FeretsAngleFeature.NAME)
public class DefaultFeretAngleFeature implements FeretsAngleFeature {

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

		if (p1.getDoublePosition(0) == p2.getDoublePosition(0)) {
			out = (90);
		}

		// tan alpha = opposite leg / adjacent leg
		// angle in radiants = atan(alpha)
		// angle in degree = atan(alpha) * (180/pi)
		final double opLeg = p2.getDoublePosition(1) - p1.getDoublePosition(1);
		final double adjLeg = p2.getDoublePosition(0) - p1.getDoublePosition(0);
		double degree = Math.atan((opLeg / adjLeg)) * (180.0 / Math.PI);
		if (adjLeg < 0) {
			degree = 180 - degree;
		}

		out = Math.abs(degree);
	}
}
