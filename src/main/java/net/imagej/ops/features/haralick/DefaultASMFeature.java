package net.imagej.ops.features.haralick;

import net.imagej.ops.Op;
import net.imagej.ops.features.haralick.HaralickFeatures.ASMFeature;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, label = "Haralick 2D: ASM")
public class DefaultASMFeature implements ASMFeature {

	@Parameter
	private CooccurrenceMatrix cooc;

	@Parameter(type = ItemIO.OUTPUT)
	private double output;

	@Override
	public void run() {
		final double[][] matrix = cooc.getOutput();
		final int nrGrayLevels = matrix.length;
		
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
