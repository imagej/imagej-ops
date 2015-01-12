package net.imagej.ops.features.geometric;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.geometric.GeometricFeatures.MinorAxisFeature;
import net.imagej.ops.features.geometric.helper.polygonhelper.MinorMajorAxisProvider;

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
	private MinorMajorAxisProvider axisProvider;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		out = axisProvider.getOutput().getA();
	}

}
