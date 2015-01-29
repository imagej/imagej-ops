package net.imagej.ops.features.haralick;

import net.imagej.ops.Op;
import net.imagej.ops.features.haralick.HaralickFeatures.ClusterShadeFeature;
import net.imagej.ops.features.haralick.helper.CoocStdX;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

// cluster shade (from cellcognition)
// https://github.com/CellCognition/cecog/blob/master/csrc/include/cecog/features.hxx#L495
@Plugin(type = Op.class, label = "Haralick 2D: Clustershade")
public class DefaultClusterShadeFeature implements
		ClusterShadeFeature<DoubleType> {

	@Parameter
	private CooccurrenceMatrix cooc;

	@Parameter
	private CoocStdX coocStdX;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;

	@Override
	public void run() {
		if (out == null)
			out = new DoubleType();

		final double[][] matrix = cooc.getOutput();
		final int nrGrayLevels = matrix.length;
		final double stdx = coocStdX.getOutput().get();

		double output = 0;
		for (int j = 0; j < nrGrayLevels; j++) {
			output += Math.pow(2 * j - 2 * stdx, 3) * matrix[j][j];
			for (int i = j + 1; i < nrGrayLevels; i++) {
				output += 2 * Math.pow((i + j - 2 * stdx), 3) * matrix[i][j];
			}
		}
		out.setReal(output);
	}

	@Override
	public DoubleType getOutput() {
		return out;
	}

	@Override
	public void setOutput(DoubleType output) {
		out = output;
	}
}
