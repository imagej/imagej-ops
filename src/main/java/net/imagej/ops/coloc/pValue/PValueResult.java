
package net.imagej.ops.coloc.pValue;

/*
 * Helper method for PValue op... allows for three variables:
 * 1) pValue 2) colocalization measure value and
 * 3) an array of all colocalization measures for shuffled images.
 */
public class PValueResult {

	private Double pValue;
	private Double colocValue;
	private double[] colocValuesArray;
	
	public PValueResult() {
		this.pValue = 0.0;
		this.colocValue = 0.0;
	}

	public Double getPValue() {
		return pValue;
	}

	public Double getColocValue() {
		return colocValue;
	}

	public double[] getColocValuesArray() {
		return colocValuesArray;
	}

	public void setPValue(final Double p) {
		this.pValue = p;
	}

	public void setColocValue(final Double c) {
		this.colocValue = c;
	}

	public void setColocValuesArray(final double[] a) {
		this.colocValuesArray = a;
	}
}
