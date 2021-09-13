/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2021 ImageJ2 developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package net.imagej.ops.image.cooccurrenceMatrix;

/**
 * Orientation of a {@link CooccurrenceMatrix3D}
 * 
 * @author Christian Dietz (University of Konstanz)
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
