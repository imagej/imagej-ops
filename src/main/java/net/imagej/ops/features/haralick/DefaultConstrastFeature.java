package net.imagej.ops.features.haralick;

import net.imagej.ops.Op;
import net.imagej.ops.features.haralick.HaralickFeatures.ContrastFeature;
import net.imagej.ops.features.haralick.helper.CoocPXMinusY;
import net.imagej.ops.features.haralick.helper.CooccurrenceMatrix;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, label = "Haralick 2D: Contrast")
public class DefaultConstrastFeature implements ContrastFeature<DoubleType> {

	@Parameter
	private CooccurrenceMatrix matrix;

	@Parameter
	private CoocPXMinusY coocPXMinusY;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType out;

	@Override
	public void run() {
		if (out == null)
			out = new DoubleType();

		final int nrGrayLevels = matrix.getOutput().length;
		final double[] pxminusxy = coocPXMinusY.getOutput();

		double output = 0;
		for (int k = 0; k <= nrGrayLevels - 1; k++) {
			output += k * k * pxminusxy[k];
		}

		out.set(output);
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
