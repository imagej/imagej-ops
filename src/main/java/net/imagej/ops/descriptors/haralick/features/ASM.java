package net.imagej.ops.descriptors.haralick.features;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imagej.ops.histogram.CooccurrenceMatrix;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Op.class, label = "Haralick2D: ASM")
public class ASM implements OutputOp<DoubleType> {

	@Parameter
	private CooccurrenceMatrix matrix;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType output;

	@Override
	public DoubleType getOutput() {
		return output;
	}

	@Override
	public void run() {
		final int nrGrayLevels = matrix.getLength();

		double res = 0;
		for (int i = 0; i < nrGrayLevels; i++) {
			for (int j = 0; j < nrGrayLevels; j++) {
				res += matrix.getValueAt(i, j) * matrix.getValueAt(i, j);
			}
		}

		output = new DoubleType(res);
	}

}
