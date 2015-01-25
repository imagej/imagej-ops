package net.imagej.ops.features.moments.normalizedmoments;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment03Feature;
import net.imagej.ops.features.moments.helper.CentralMomentsHelper;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link NormalizedCentralMoment03Feature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = NormalizedCentralMoment03Feature.NAME)
public class DefaultNormalizedCentralMoment03Feature implements
NormalizedCentralMoment03Feature {

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
		out = momentsHelper.getOutput().getCentralMoment03()
				/ Math.pow(momentsHelper.getOutput().getCentralMoment00(),
						1 + ((0 + 3) / 2));
	}
}
