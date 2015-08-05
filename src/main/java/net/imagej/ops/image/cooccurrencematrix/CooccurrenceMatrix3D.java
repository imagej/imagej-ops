package net.imagej.ops.image.cooccurrencematrix;

import java.util.List;

import net.imagej.ops.AbstractFunctionOp;
import net.imagej.ops.Contingent;
import net.imagej.ops.OpService;
import net.imagej.ops.Ops.Image.CooccurrenceMatrix;
import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.type.numeric.RealType;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Calculates coocccurrence matrix from an 3D-{@link IterableInterval}.
 * 
 * @author Stephan Sellien, University of Konstanz
 * @author Andreas Graumann, University of Konstanz
 * @author Christian Dietz, University of Konstanz
 */
@Plugin(type = CooccurrenceMatrix.class, name = CooccurrenceMatrix.NAME)
public class CooccurrenceMatrix3D<T extends RealType<T>> extends
		AbstractFunctionOp<IterableInterval<T>, double[][]> implements
		CooccurrenceMatrix, Contingent {

	@Parameter
	private OpService ops;

	@Parameter(label = "Number of Gray Levels", min = "0", max = "128", stepSize = "1", initializer = "32")
	private int nrGreyLevels;

	@Parameter(label = "Distance", min = "0", max = "128", stepSize = "1", initializer = "1")
	private int distance;

	@Parameter(label = "Matrix Orientation")
	private MatrixOrientation orientation;

	@Override
	public double[][] compute(final IterableInterval<T> input) {

		double[][] matrix = new double[nrGreyLevels][nrGreyLevels];

		final Cursor<T> cursor = input.cursor();
		final List<T> minMax = ops.stats().minMax(input);

		double localMin = minMax.get(0).getRealDouble();
		double localMax = minMax.get(1).getRealDouble();

		final int[][][] pixels = new int[(int) input.dimension(2)][(int) input
				.dimension(1)][(int) input.dimension(0)];

		while (cursor.hasNext()) {
			cursor.fwd();
			pixels[cursor.getIntPosition(2) - (int) input.min(2)][cursor
					.getIntPosition(1) - (int) input.min(1)][cursor
					.getIntPosition(0) - (int) input.min(0)] = (int) (((cursor
					.get().getRealDouble() - localMin) / (localMax - localMin)) * (nrGreyLevels - 1));
		}

		int nrPairs = 0;

		for (int z = 0; z < pixels.length; z++) {
			for (int y = 0; y < pixels[z].length; y++) {
				for (int x = 0; x < pixels[z][y].length; x++) {

					// ignore pixels not in mask
					if (pixels[z][y][x] == Integer.MAX_VALUE) {
						continue;
					}

					// get second pixel
					final int sx = x + orientation.getValueAtDim(0) * distance;
					final int sy = y + orientation.getValueAtDim(1) * distance;
					final int sz = z + orientation.getValueAtDim(2) * distance;

					// second pixel in interval and mask
					if (sx >= 0 && sy >= 0 && sz >= 0 && sz < pixels.length
							&& sy < pixels[sz].length
							&& sx < pixels[sz][sy].length) {

						matrix[pixels[z][y][x]][pixels[sz][sy][sx]]++;
						nrPairs++;

					}
				}
			}
		}

		// normalize matrix
		if (nrPairs > 0) {
			double divisor = 1.0 / nrPairs;
			for (int row = 0; row < matrix.length; row++) {
				for (int col = 0; col < matrix[row].length; col++) {
					matrix[row][col] *= divisor;
				}
			}
		}

		return matrix;
	}

	@Override
	public boolean conforms() {
		return getInput().numDimensions() == 3 && orientation.isCompatible(3);
	}
}
