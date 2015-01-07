package net.imagej.ops.features.moments.normalmoments;

import net.imagej.ops.Op;
import net.imagej.ops.features.FeatureService;
import net.imagej.ops.features.moments.ImageMomentFeatures.Moment11Feature;
import net.imagej.ops.features.moments.helper.NormalMomentsHelper;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Generic implementation of {@link Moment11Feature}. Use {@link FeatureService}
 * to compile this {@link Op}.
 * 
 * @author Daniel Seebacher, University of Konstanz.
 */
@Plugin(type = Op.class, name = Moment11Feature.NAME)
public class DefMoment11 implements Moment11Feature {

	
	@Parameter(type = ItemIO.INPUT)
	private NormalMomentsHelper momentsHelper;

	@Parameter(type = ItemIO.OUTPUT)
	private double out;

	@Override
	public double getFeatureValue() {
		return out;
	}

	@Override
	public void run() {
		out = momentsHelper.getOutput().getMoment11();
	}

}
