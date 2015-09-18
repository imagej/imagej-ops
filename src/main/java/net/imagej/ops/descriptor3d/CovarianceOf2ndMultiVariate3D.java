package net.imagej.ops.descriptor3d;

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
	
	public double[] getEigenvector(int i) {
		if (ed == null) {
			ed = new EigenDecomposition(matrix);
		}
		return ed.getEigenvector(i).toArray();
	}
	
	public double getEigenvalue(int i) {
		if (ed == null) {
			ed = new EigenDecomposition(matrix);
		}
		return ed.getRealEigenvalue(i);
	}

	public double getS200() {
		return matrix.getEntry(0, 0);
	}

	public void setS200(double s200) {
		matrix.setEntry(0, 0, s200);
	}

	public double getS110() {
		return matrix.getEntry(0, 1);
	}

	public void setS110(double s110) {
		matrix.setEntry(1, 0, s110);
		matrix.setEntry(0, 1, s110);
	}

	public double getS101() {
		return matrix.getEntry(0, 2);
	}

	public void setS101(double s101) {
		matrix.setEntry(2, 0, s101);
		matrix.setEntry(0, 2, s101);
	}

	public double getS020() {
		return matrix.getEntry(1, 1);
	}

	public void setS020(double s020) {
		matrix.setEntry(1, 1, s020);
	}

	public double getS011() {
		return matrix.getEntry(1, 2);
	}

	public void setS011(double s011) {
		matrix.setEntry(2, 1, s011);
		matrix.setEntry(1, 2, s011);
	}

	public double getS002() {
		return matrix.getEntry(2, 2);
	}

	public void setS002(double s002) {
		matrix.setEntry(2, 2, s002);
	}

}
