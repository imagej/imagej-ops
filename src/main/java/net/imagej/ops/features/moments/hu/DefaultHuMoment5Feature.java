package net.imagej.ops.features.moments.hu;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.moments.ImageMomentFeatures.HuMoment5Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment02Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment03Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment11Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment12Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment20Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment21Feature;
import net.imagej.ops.features.moments.ImageMomentFeatures.NormalizedCentralMoment30Feature;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link HuMoment5Feature}. Use
 * {@link FeatureService} to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = HuMoment5Feature.NAME)
public class DefaultHuMoment5Feature implements HuMoment5Feature {

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment02Feature n02;

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment03Feature n03;

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment11Feature n11;

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment12Feature n12;

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment20Feature n20;

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment21Feature n21;

	@Parameter(type = ItemIO.INPUT)
	private NormalizedCentralMoment30Feature n30;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		out = (n30.getFeatureValue() - 3 * n12.getFeatureValue())
				* (n30.getFeatureValue() + n12.getFeatureValue())
				* (Math.pow(n30.getFeatureValue() + n12.getFeatureValue(), 2) - 3 * Math
						.pow(n21.getFeatureValue() + n03.getFeatureValue(), 2))
				+ (3 * n21.getFeatureValue() - n03.getFeatureValue())
				* (n21.getFeatureValue() + n03.getFeatureValue())
				* (3 * Math.pow(n30.getFeatureValue() + n12.getFeatureValue(),
						2) - Math.pow(
						n21.getFeatureValue() + n03.getFeatureValue(), 2));
	}
}
