package net.imagej.ops.features.haralick;

import net.imagej.ops.Op;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickASMFeature;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, label = "Haralick2D: ASM")
public class DefHaralickASMFeature implements HaralickASMFeature {

	@Parameter
	private CooccurrenceMatrix cooc;

	@Parameter(type = ItemIO.OUTPUT)
	private double output;

	@Override
	public void run() {
		final int nrGrayLevels = cooc.getNrGreyLevels();
		final double[][] matrix = cooc.getMatrix();

		output = 0;
		for (int i = 0; i < nrGrayLevels; i++) {
			for (int j = 0; j < nrGrayLevels; j++) {
				output += matrix[i][j] * matrix[i][j];
			}
		}
	}

	@Override
	public double getFeatureValue() {
		return output;
	}

}
