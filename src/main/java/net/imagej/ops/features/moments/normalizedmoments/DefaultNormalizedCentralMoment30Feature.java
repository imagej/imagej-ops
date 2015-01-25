package net.imagej.ops.features.moments.normalizedmoments;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment30Feature;
import net.imagej.ops.features.moments.helper.CentralMomentsHelper;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link NormalizedCentralMoment30Feature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = NormalizedCentralMoment30Feature.NAME)
public class DefaultNormalizedCentralMoment30Feature implements
		NormalizedCentralMoment30Feature {

	@Parameter(type = ItemIO.INPUT)
	private CentralMomentsHelper momentsHelper;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		out = momentsHelper.getOutput().getCentralMoment30()
				/ Math.pow(momentsHelper.getOutput().getCentralMoment00(),
						1 + ((3 + 0) / 2));
	}
}
