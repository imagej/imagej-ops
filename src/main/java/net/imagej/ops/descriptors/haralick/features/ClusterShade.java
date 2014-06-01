
package net.imagej.ops.descriptors.haralick.features;

import net.imagej.ops.Op;
import net.imagej.ops.OutputOp;
import net.imagej.ops.descriptors.descriptorsets.CoocParameter;
import net.imagej.ops.descriptors.haralick.helpers.CoocStdX;
import net.imglib2.ops.data.CooccurrenceMatrix;
import net.imglib2.type.numeric.real.DoubleType;

import org.scijava.ItemIO;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

// cluster shade (from cellcognition)
// https://github.com/CellCognition/cecog/blob/master/csrc/include/cecog/features.hxx#L495
@Plugin(type = Op.class, label = "Haralick2D: Clustershade")
public class ClusterShade implements OutputOp<DoubleType> {

	@Parameter
	private CoocParameter param;

	@Parameter
	private CooccurrenceMatrix matrix;

	@Parameter
	private CoocStdX coocStdX;

	@Parameter(type = ItemIO.OUTPUT)
	private DoubleType output;

	@Override
	public DoubleType getOutput() {
		return output;
	}

	@Override
	public void run() {
		final int nrGrayLevels = param.getNrGrayLevels();
		final double stdx = coocStdX.getOutput();

		double res = 0.0d;
		for (int j = 0; j < nrGrayLevels; j++) {
			res += Math.pow(2 * j - 2 * stdx, 3) * matrix.getValueAt(j, j);
			for (int i = j + 1; i < nrGrayLevels; i++) {
				res += 2 * Math.pow((i + j - 2 * stdx), 3)
						* matrix.getValueAt(i, j);
			}
		}

		output = new DoubleType(res);
	}
}
