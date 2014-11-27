package net.imagej.ops.features.haralick;

import net.imagej.ops.Op;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickContrastFeature;
import net.imagej.ops.features.haralick.helper.CoocPXMinusY;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, label = "Haralick 2D: Contrast")
public class DefHaralickConstrastFeature implements HaralickContrastFeature {

	@Parameter
	private CooccurrenceMatrix matrix;

	@Parameter
	private CoocPXMinusY coocPXMinusY;

	@Parameter(type = ItemIO.OUTPUT)
	private double output;
	
	@Override
	public void run() {
		final int nrGrayLevels = matrix.getOutput().length;
		final double[] pxminusxy = coocPXMinusY.getOutput();

		output = 0;
		for (int k = 0; k <= nrGrayLevels - 1; k++) {
			output += k * k * pxminusxy[k];
		}
	}

	@Override
	public double getFeatureValue() {
		return output;
	}

}
