package net.imagej.ops.image.cooccurrencematrix;

/**
 * Orientation of a {@link CooccurrenceMatrix3D}
 * 
 * @author Christian Dietz, University of Konstanz
 *
 */
public enum MatrixOrientation3D implements MatrixOrientation {
	// 2D directions
	HORIZONTAL(1, 0, 0), VERTICAL(0, 1, 0), DIAGONAL(-1, 1, 0), ANTIDIAGONAL(1,
			1, 0),

	// 3D Horizontal directions
	HORIZONTAL_VERTICAL(1, 0, 1), HORIZONTAL_DIAGONAL(1, 0, -1),

	// 3D Vertical directions
	VERTICAL_VERTICAL(0, 1, 1), VERTICAL_DIAGONAL(0, 1, -1),

	// 3D Diagonal directions
	DIAGONAL_VERTICAL(-1, 1, 1), DIAGONAL_DIAGONAL(-1, 1, -1),

	// 3D Antidiagonal directions
	ANTIDIAGONAL_VERTICAL(1, 1, 1), ANTIDIAGONAL_DIAGONAL(1, 1, -1),

	// 3D depth direction
	DEPTH(0, 0, 1);

	private int[] d;

	private MatrixOrientation3D(int... d) {
		this.d = d;
	}

	@Override
	public boolean isCompatible(int numDims) {
		return numDims == 3;
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
		return 3;
	}
}
