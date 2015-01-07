package net.imagej.ops.features.moments.centralmoments;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.moments.ImageMomentFeatures.CentralMoment11Feature;
import net.imagej.ops.features.moments.helper.CentralMomentsHelper;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link CentralMoment11Feature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = CentralMoment11Feature.NAME)
public class DefCentralMoment11 implements CentralMoment11Feature {

	@Parameter(type = ItemIO.INPUT)
	private CentralMomentsHelper centralMomentsHelper;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		out = centralMomentsHelper.getOutput().getCentralMoment11();
	}

}
