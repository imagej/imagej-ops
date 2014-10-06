package net.imagej.ops.features.haralick;

import net.imagej.ops.Op;
import net.imagej.ops.features.haralick.HaralickFeatures.HaralickClusterPromenenceFeature;
import net.imagej.ops.features.haralick.helper.CoocStdX;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

// cluster promenence (from cellcognition)
// https://github.com/CellCognition/cecog/blob/master/csrc/include/cecog/features.hxx#L479
@Plugin(type = Op.class, label = "Haralick2D: Cluster Promenence")
public class DefHaralickClusterPromenenceFeature implements
		HaralickClusterPromenenceFeature {

	@Parameter
	private CooccurrenceMatrix cooc;

	@Parameter
	private CoocStdX coocStdX;

	@Parameter(type = ItemIO.OUTPUT)
	private double output;

	@Override
	public double getFeatureValue() {
		return output;
	}

	@Override
	public void run() {
		final int nrGrayLevels = cooc.getNrGreyLevels();
		final double[][] matrix = cooc.getMatrix();
		final double stdx = coocStdX.getOutput();

		output = 0;
		for (int j = 0; j < nrGrayLevels; j++) {
			output += Math.pow(2 * j - 2 * stdx, 4) * matrix[j][j];
			for (int i = j + 1; i < nrGrayLevels; i++) {
				output += 2 * Math.pow((i + j - 2 * stdx), 4) * matrix[i][j];
			}
		}

	}
}
