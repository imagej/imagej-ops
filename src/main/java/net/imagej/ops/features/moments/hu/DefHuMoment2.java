package net.imagej.ops.features.moments.hu;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment2Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment02Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment11Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment20Feature;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link HuMoment2Feature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = HuMoment2Feature.NAME)
public class DefHuMoment2 implements HuMoment2Feature {

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment11Feature n11;

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment02Feature n02;

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment20Feature n20;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		out = Math.pow(n20.getFeatureValue() - n02.getFeatureValue(), 2) - 4
				* (Math.pow(n11.getFeatureValue(), 2));
	}
}
