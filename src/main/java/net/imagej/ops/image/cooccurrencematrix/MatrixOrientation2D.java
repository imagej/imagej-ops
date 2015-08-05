package net.imagej.ops.image.cooccurrencematrix;

/**
 * Orientation of a {@link CooccurrenceMatrix2D}
 * 
 * @author Christian Dietz, University of Konstanz
 *
 */
public enum MatrixOrientation2D implements MatrixOrientation {
	DIAGONAL(1, -1), ANTIDIAGONAL(1, 1), HORIZONTAL(1, 0), VERTICAL(0, 1);

	private int[] d;

	private MatrixOrientation2D(int... d) {
		this.d = d;
	}

	@Override
	public boolean isCompatible(int numDims) {
		return numDims == 2;
	}

	@Override
	public MatrixOrientation getByName(String name) {
		return valueOf(name);
	}

	@Override
	public int getValueAtDim(int d) {
		return this.d[d];
	}

	@Override
	public int numDims() {
		return 2;
	}
}
