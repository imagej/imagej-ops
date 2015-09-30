/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2014 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, University of Konstanz and Brian Northan.
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
package net.imagej.ops.geom;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

/**
 * This class stores the covariance matrix of the 2nd multi variate 3D
 * and the eigen decomposition of this matrix.
 * 
 * @author Tim-Oliver Buchholz, University of Konstanz.
 *
 */
public class CovarianceOf2ndMultiVariate3D {
	
	private Array2DRowRealMatrix matrix;
	private EigenDecomposition ed = null;

	public CovarianceOf2ndMultiVariate3D() {		
		matrix = new Array2DRowRealMatrix(3, 3);
		matrix.setEntry(0, 0, 0);
		matrix.setEntry(0, 1, 0);
		matrix.setEntry(0, 2, 0);
		
		matrix.setEntry(1, 0, 0);
		matrix.setEntry(1, 1, 0);
		matrix.setEntry(1, 2, 0);
		
		matrix.setEntry(2, 0, 0);
		matrix.setEntry(2, 1, 0);
		matrix.setEntry(2, 2, 0);		
	}
	
	public Array2DRowRealMatrix getCovarianceMatrix() {
		return matrix;
	}
	
	public double[] getEigenvector(final int i) {
		if (ed == null) {
			ed = new EigenDecomposition(matrix);
		}
		return ed.getEigenvector(i).toArray();
	}
	
	public double getEigenvalue(final int i) {
		if (ed == null) {
			ed = new EigenDecomposition(matrix);
		}
		return ed.getRealEigenvalue(i);
	}

	public double getS200() {
		return matrix.getEntry(0, 0);
	}

	public void setS200(final double s200) {
		matrix.setEntry(0, 0, s200);
	}

	public double getS110() {
		return matrix.getEntry(0, 1);
	}

	public void setS110(final double s110) {
		matrix.setEntry(1, 0, s110);
		matrix.setEntry(0, 1, s110);
	}

	public double getS101() {
		return matrix.getEntry(0, 2);
	}

	public void setS101(final double s101) {
		matrix.setEntry(2, 0, s101);
		matrix.setEntry(0, 2, s101);
	}

	public double getS020() {
		return matrix.getEntry(1, 1);
	}

	public void setS020(final double s020) {
		matrix.setEntry(1, 1, s020);
	}

	public double getS011() {
		return matrix.getEntry(1, 2);
	}

	public void setS011(final double s011) {
		matrix.setEntry(2, 1, s011);
		matrix.setEntry(1, 2, s011);
	}

	public double getS002() {
		return matrix.getEntry(2, 2);
	}

	public void setS002(final double s002) {
		matrix.setEntry(2, 2, s002);
	}

}
