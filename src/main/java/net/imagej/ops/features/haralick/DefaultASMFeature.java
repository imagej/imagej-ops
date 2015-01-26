package net.imagej.ops.features.haralick;

import net.imagej.ops.Op;
import net.imagej.ops.features.haralick.HaralickFeatures.ASMFeature;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, label = "Haralick 2D: ASM")
public class DefaultASMFeature implements ASMFeature<DoubleType> {

	@Parameter
	private CooccurrenceMatrix cooc;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;

	@Override
	public void run() {
		if (out == null)
			out = new DoubleType();

		final double[][] matrix = cooc.getOutput();
		final int nrGrayLevels = matrix.length;

		double res = 0;
		for (int i = 0; i < nrGrayLevels; i++) {
			for (int j = 0; j < nrGrayLevels; j++) {
				res += matrix[i][j] * matrix[i][j];
			}
		}
		out.setReal(res);
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
