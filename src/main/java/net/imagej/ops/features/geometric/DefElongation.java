package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.geometric.GeometricFeatures.ElongationFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MajorAxisFeature;
import net.imagej.ops.features.geometric.GeometricFeatures.MinorAxisFeature;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link ElongationFeature}. Use {@link FeatureService} to
 * compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = ElongationFeature.NAME)
public class DefElongation implements ElongationFeature {

	@Parameter(type = ItemIO.INPUT)
	private MajorAxisFeature majorAxis;

	@Parameter(type = ItemIO.INPUT)
	private MinorAxisFeature minorAxis;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		out = 1d - (minorAxis.getFeatureValue() / majorAxis.getFeatureValue());
	}
}
