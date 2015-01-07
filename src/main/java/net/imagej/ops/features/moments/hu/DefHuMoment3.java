package net.imagej.ops.features.moments.hu;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment3Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment03Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment12Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment21Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment30Feature;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link HuMoment3Feature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = HuMoment3Feature.NAME)
public class DefHuMoment3 implements HuMoment3Feature {

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment30Feature n30;

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment12Feature n12;

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment21Feature n21;

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment03Feature n03;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		out = Math.pow(n30.getFeatureValue() - 3 * n12.getFeatureValue(), 2)
				+ Math.pow(3 * n21.getFeatureValue() - n03.getFeatureValue(), 2);
	}
}
