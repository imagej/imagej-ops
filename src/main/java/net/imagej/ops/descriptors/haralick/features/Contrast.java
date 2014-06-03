package net.imagej.ops.descriptors.haralick.features;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imagej.ops.descriptors.haralick.helpers.CoocPXMinusY;
import net.imagej.ops.histogram.CooccurrenceMatrix;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, label = "Haralick2D: Contrast")
public class Contrast implements OutputOp<DoubleType> {

	@Parameter
	private CooccurrenceMatrix matrix;

	@Parameter
	private CoocPXMinusY coocPXMinusZ;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType output;

	@Override
	public DoubleType getOutput() {
		return output;
	}

	@Override
	public void run() {
		final int nrGrayLevels = matrix.getLength();
		final double[] pxminusxy = coocPXMinusZ.getOutput();

		double res = 0;
		for (int k = 0; k <= nrGrayLevels - 1; k++) {
			res += k * k * pxminusxy[k];
		}

		output = new DoubleType(res);
	}

}
