/*
 * #%L
 * SciJava OPS: a framework for reusable algorithms.
 * %%
 * Copyright (C) 2013 Board of Regents of the University of
 * Wisconsin-Madison, and University of Konstanz.
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
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */
package net.imagej.ops.descriptors.haralick.helpers;

import net.imagej.ops.Op;
import net.imglib2.ops.data.CooccurrenceMatrix.MatrixOrientation;

import org.scijava.plugin.Parameter;

/**
 * 
 */
public class CoocParameter implements Op {
	
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

	@Override
	public void run() {
		// nothing to do here
	}
}
