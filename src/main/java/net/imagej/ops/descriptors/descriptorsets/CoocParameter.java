package net.imagej.ops.descriptors.descriptorsets;

import net.imagej.ops.descriptors.DescriptorParameters;
import net.imglib2.ops.data.CooccurrenceMatrix.MatrixOrientation;

import org.scijava.plugin.Parameter;

/**
 * TODO
 */
public class CoocParameter implements DescriptorParameters {

	@Parameter(label = "Number of Gray Levels", min = "0", max = "128", stepSize = "1", initializer = "32")
	public double nrGrayLevels;

	@Parameter(label = "Distance", min = "0", max = "128", stepSize = "1", initializer = "1")
	public double distance;

	@Parameter(label = "Matrix Orientation", choices = { "DIAGONAL",
			"ANTIDIAGONAL", "HORIZONTAL", "VERTICAL" })
	public String orientation;

	public void setNrGrayLevels(final int _nrGrayLevels) {
		this.nrGrayLevels = _nrGrayLevels;
	}

	public void setDistance(final int _distance) {
		this.distance = _distance;
	}

	// TODO: Make this an enum or at least add a helper for
	// CooccurenceMatrix.MatrixOrientation
	public void setOrientation(final String _orientation) {
		this.orientation = _orientation;
	}

	public int getNrGrayLevels() {
		return (int) nrGrayLevels;
	}

	public int getDistance() {
		return (int) distance;
	}

	public MatrixOrientation getOrientation() {
		if (orientation == "DIAGONAL") {
			return MatrixOrientation.DIAGONAL;
		} else if (orientation == "ANTIDIAGONAL") {
			return MatrixOrientation.ANTIDIAGONAL;
		} else if (orientation == "HORIZONTAL") {
			return MatrixOrientation.HORIZONTAL;
		} else {
			return MatrixOrientation.VERTICAL;
		}
	}
}
