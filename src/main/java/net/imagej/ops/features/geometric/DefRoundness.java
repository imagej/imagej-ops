package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.geometric.GeometricFeatures.AreaFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MajorAxisFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MinorAxisFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.RoundnessFeature;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link RoundnessFeature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = RoundnessFeature.NAME)
public class DefRoundness implements RoundnessFeature {

	@Parameter(type = ItemIO.INPUT)
	private AreaFeature area;

	@Parameter(type = ItemIO.INPUT)
	private MajorAxisFeature majorAxis;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		out = 4 * (area.getFeatureValue() / (Math.PI * Math.pow(
				majorAxis.getFeatureValue(), 2)));
	}
}
